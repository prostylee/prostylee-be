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
import vn.prostylee.store.service.StoreService;
import vn.prostylee.voucher.dto.filter.VoucherUserFilter;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.dto.request.VoucherUserRequest;
import vn.prostylee.voucher.dto.response.VoucherCalculationResponse;
import vn.prostylee.voucher.dto.response.VoucherUserResponse;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.entity.VoucherUser;
import vn.prostylee.voucher.repository.VoucherExtRepository;
import vn.prostylee.voucher.repository.VoucherRepository;
import vn.prostylee.voucher.repository.VoucherUserRepository;
import vn.prostylee.voucher.service.VoucherCalculatorService;
import vn.prostylee.voucher.service.VoucherUserService;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoucherUserServiceImpl implements VoucherUserService {

    private final VoucherRepository voucherRepository;
    private final VoucherUserRepository voucherUserRepository;
    private final VoucherExtRepository voucherExtRepository;
    private final StoreService storeService;
    private final List<VoucherConditionVerifier> voucherConditionVerifiers;
    private final VoucherCalculatorService voucherCalculatorService;

    @Override
    public Page<VoucherUserResponse> findAll(BaseFilter baseFilter) {
        VoucherUserFilter filter = (VoucherUserFilter) baseFilter;
        Page<VoucherUserResponse> page = voucherExtRepository.findAll(filter);
        return page.map(response -> {
            response.setStoreOwner(storeService.getStoreResponseLite(response.getId()));
            return response;
        });
    }

    private VoucherUserResponse toResponse(Voucher voucher, Long userVoucherId) {
        VoucherUserResponse response = BeanUtil.copyProperties(voucher, VoucherUserResponse.class);
        response.setStoreOwner(storeService.getStoreResponseLite(voucher.getId()));
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
            return voucherCalculatorService.calculateDiscount(voucher, request);
        }

        log.info("Voucher is invalid with voucherId={}, request={}", voucherId, request);
        return VoucherCalculationResponse.builder()
                .voucherId(voucherId)
                .code(null)
                .build();
    }

    @Override
    public boolean verifyVoucher(Long voucherId, VoucherCalculationRequest request) {
        try {
            Voucher voucher = findVoucherById(voucherId);
            return voucherConditionVerifiers.stream()
                    .allMatch(verifier -> verifier.isSatisfyCondition(voucher, request));
        } catch (ResourceNotFoundException e) {
            return false;
        }
    }

    @Override
    public int countVoucherByUser(Long voucherId, Long userId) {
        return voucherUserRepository.countVoucher(voucherId, userId);
    }

    @Override
    public int countAllVouchers(Long voucherId) {
        return voucherUserRepository.countVoucher(voucherId, null);
    }

    private Voucher findVoucherById(Long voucherId) {
        return voucherRepository.findOneActive(voucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher is not found with id [" + voucherId + "]"));
    }
}
