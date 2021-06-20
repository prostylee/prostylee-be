package vn.prostylee.voucher.verifier.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.CollectionExtUtils;
import vn.prostylee.location.service.AddressService;
import vn.prostylee.voucher.constant.CustomerType;
import vn.prostylee.voucher.dto.request.VoucherCalculationRequest;
import vn.prostylee.voucher.dto.request.VoucherOrderRequest;
import vn.prostylee.voucher.dto.request.VoucherShippingAddressRequest;
import vn.prostylee.voucher.entity.Voucher;
import vn.prostylee.voucher.verifier.VoucherConditionVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Order(80)
@Component
public class CustomerAddressConditionVerifier implements VoucherConditionVerifier {

    private final AddressService addressService;
    private final AuthenticatedProvider authenticatedProvider;

    @Override
    public boolean isSatisfyCondition(Voucher voucher, VoucherCalculationRequest request) {
        CustomerType type = CustomerType.findByCustomerType(voucher.getCndCustomerType()).orElse(null);
        if (type == null) {
            return true;
        }
        switch (type) {
            case LIMIT_LOCATION:
                return verifyByCustomerAddress(voucher, request);

            case LIMIT_MEMBER:
            case LIMIT_POTENTIAL_CUSTOMER:
            case LIMIT_NEW_CUSTOMER:
                // TODO define criteria
                return authenticatedProvider.getUserId().isPresent();

            case LIMIT_SPECIFIC_CUSTOMER:
                return verifyBySpecificCustomer(voucher, request.getOrder().getBuyerId());

            case ALL:
            default:
                return true;
        }
    }

    private boolean verifyBySpecificCustomer(Voucher voucher, Long userId) {
        if (CollectionUtils.isEmpty(voucher.getCndCustomerUserIds())) {
            return false;
        }

        return CollectionExtUtils.contains(voucher.getCndCustomerUserIds(), userId);
    }

    private boolean verifyByCustomerAddress(Voucher voucher, VoucherCalculationRequest request) {
        List<Long> belongToIds = voucher.getCndCustomerAddressIds();
        if (CollectionUtils.isEmpty(belongToIds)) {
            return true;
        }

        VoucherShippingAddressRequest shippingAddress = Optional.ofNullable(request)
                .map(VoucherCalculationRequest::getOrder)
                .map(VoucherOrderRequest::getShippingAddress)
                .orElse(null);
        if (shippingAddress == null) {
            return false;
        }

        String[] codes = {
                shippingAddress.getCity(),
                shippingAddress.getState(),
                shippingAddress.getCountry(),
                shippingAddress.getZipcode(),
        };
        String lowestCode = Arrays.stream(codes).filter(StringUtils::isNotBlank).findFirst().orElse(null);
        return addressService.isBelongToIds(lowestCode, belongToIds);
    }
}
