package vn.prostylee.voucher.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceInUsedException;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.store.service.StoreService;
import vn.prostylee.voucher.constant.DiscountType;
import vn.prostylee.voucher.dto.filter.VoucherUserFilter;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.dto.request.VoucherOrderDetailRequest;
import vn.prostylee.voucher.dto.request.VoucherOrderRequest;
import vn.prostylee.voucher.dto.request.VoucherUserRequest;
import vn.prostylee.voucher.dto.response.VoucherCalculationResponse;
import vn.prostylee.voucher.dto.response.VoucherOrderDetailResponse;
import vn.prostylee.voucher.dto.response.VoucherOrderResponse;
import vn.prostylee.voucher.dto.response.VoucherUserResponse;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.entity.VoucherUser;
import vn.prostylee.voucher.repository.VoucherExtRepository;
import vn.prostylee.voucher.repository.VoucherRepository;
import vn.prostylee.voucher.repository.VoucherUserRepository;
import vn.prostylee.voucher.service.VoucherUserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoucherUserServiceImpl implements VoucherUserService {

    private final VoucherRepository voucherRepository;
    private final VoucherUserRepository voucherUserRepository;
    private final VoucherExtRepository voucherExtRepository;
    private final StoreService storeService;
    private final ProductService productService;

    @Override
    public Page<VoucherUserResponse> findAll(BaseFilter baseFilter) {
        VoucherUserFilter filter = (VoucherUserFilter) baseFilter;
        Page<Voucher> page = voucherExtRepository.findAll(filter);
        return page.map(this::toResponse);
    }

    private VoucherUserResponse toResponse(Voucher voucher) {
        VoucherUserResponse response = BeanUtil.copyProperties(voucher, VoucherUserResponse.class);
        response.setStoreOwner(storeService.getStoreResponseLite(voucher.getId()));
        return response;
    }

    private VoucherUserResponse toResponse(Voucher voucher, Long userVoucherId) {
        VoucherUserResponse response = toResponse(voucher);
        response.setSavedUserVoucherId(userVoucherId);
        return response;
    }

    @Override
    public VoucherUserResponse findById(Long id) {
        VoucherUser voucherUser = getById(id);
        Voucher voucher = findVoucherById(voucherUser.getVoucherId());
        return toResponse(voucher, voucherUser.getId());
    }

    @Override
    public VoucherUserResponse save(VoucherUserRequest voucherRequest) {
        Voucher voucher = findVoucherById(voucherRequest.getVoucherId());
        return save(voucherRequest, voucher);
    }

    private VoucherUserResponse save(VoucherUserRequest voucherRequest, Voucher voucher) {
        if (voucher == null || BooleanUtils.isNotTrue(voucher.getActive())) {
            throw new ResourceNotFoundException("VoucherUser is not found with id [" + voucherRequest.getVoucherId() + "]");
        }
        VoucherUser entity = BeanUtil.copyProperties(voucherRequest, VoucherUser.class);
        VoucherUser savedEntity = voucherUserRepository.save(entity);
        return toResponse(voucher, savedEntity.getId());
    }

    @Override
    public VoucherUserResponse update(Long id, VoucherUserRequest request) {
        VoucherUser entity = getById(id);
        BeanUtil.mergeProperties(request, entity);
        VoucherUser savedEntity = voucherUserRepository.save(entity);

        Voucher voucher = findVoucherById(savedEntity.getVoucherId());
        return toResponse(voucher, savedEntity.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            VoucherUser entity = getById(id);
            if (entity.getUsedAt() != null) {
                throw new ResourceInUsedException("Voucher is in used, could not delete. VoucherId=" + entity.getVoucherId());
            }
            voucherUserRepository.deleteById(id);
            log.info("VoucherUser with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("VoucherUser id [{}] does not exists", id);
            return false;
        }
    }

    private VoucherUser getById(Long id) {
        return voucherUserRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("VoucherUser is not found with id [" + id + "]"));
    }

    @Override
    public VoucherUserResponse saveByVoucherId(Long voucherId) {
        Voucher voucher = findVoucherById(voucherId);
        VoucherUserRequest request = VoucherUserRequest.builder()
                .voucherId(voucher.getId())
                .voucherCode(voucher.getCode())
                .storeId(voucher.getStoreId())
                .build();
        return save(request, voucher);
    }

    @Override
    public boolean deleteByVoucherId(Long voucherId) {
        try {
            VoucherUser entity = voucherUserRepository.findTopByVoucherId(voucherId);
            if (entity.getUsedAt() != null) {
                throw new ResourceInUsedException("Voucher is in used, could not delete. VoucherId=" + voucherId);
            }
            voucherUserRepository.deleteById(voucherId);
            log.info("VoucherUser with id [{}] deleted successfully", entity.getId());
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Voucher id [{}] does not exists", voucherId);
            return false;
        }
    }

    @Override
    public VoucherCalculationResponse calculateDiscount(Long voucherId, VoucherCalculationRequest request) {
        if (verifyVoucher(voucherId, request)) {
            Voucher voucher = findVoucherById(voucherId);
            // TODO calculate based on voucher conditions
            List<VoucherOrderDetailResponse> details = calculateOrderDetails(request.getOrderDetails(), voucher);
            VoucherOrderResponse order = calculateOrder(request.getOrder(), details);
            return VoucherCalculationResponse.builder()
                    .voucherId(voucherId)
                    .code(voucher.getCode())
                    .expiredDate(voucher.getCndValidTo())
                    .order(order)
                    .orderDetails(details)
                    .build();
        }
        return null;
    }

    private VoucherOrderResponse calculateOrder(VoucherOrderRequest order, List<VoucherOrderDetailResponse> details) {
        Double amount = details.stream().map(VoucherOrderDetailResponse::getAmount).reduce(0.0, Double::sum);
        Double discountAmount = details.stream().map(VoucherOrderDetailResponse::getDiscountAmount).reduce(0.0, Double::sum);
        Double balance = amount - discountAmount;
        return VoucherOrderResponse.builder()
                .amount(amount)
                .discountAmount(discountAmount)
                .balance(balance)
                .buyerId(order.getBuyerId())
                .paymentTypeId(order.getPaymentTypeId())
                .shippingMethodId(order.getShippingMethodId())
                .shippingProviderId(order.getShippingProviderId())
                .build();
    }

    private List<VoucherOrderDetailResponse> calculateOrderDetails(List<VoucherOrderDetailRequest> orderDetails, Voucher voucher) {
        return orderDetails.stream()
                .map(detail -> {
                    ProductResponse product = productService.findById(detail.getProductId());
                    Double price = Optional.ofNullable(product.getPriceSale()).orElse(product.getPrice());
                    // TODO calculate based on voucher conditions
                    Double total = detail.getQuantity() * price;
                    Double discount = voucher.getDiscountAmount();
                    if (DiscountType.PERCENT.getType().equals(voucher.getType())) {
                        discount = total * (discount / 100);
                    }
                    if (voucher.getDiscountMaxAmount() != null && discount > voucher.getDiscountMaxAmount()) {
                        discount = voucher.getDiscountMaxAmount();
                    }
                    Double balance = total - discount;
                    return VoucherOrderDetailResponse.builder()
                            .storeId(product.getStoreId())
                            .categoryId(product.getCategoryId())
                            .productId(detail.getProductId())
                            .quantity(detail.getQuantity())
                            .amount(total)
                            .discountAmount(discount)
                            .balance(balance)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean verifyVoucher(Long voucherId, VoucherCalculationRequest request) {
        // TODO check voucher conditions
        return BooleanUtils.isTrue(findVoucherById(voucherId).getActive());
    }

    private Voucher findVoucherById(Long voucherId) {
        return voucherRepository.findOneActive(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher is not found with id [" + voucherId + "]"));
    }
}
