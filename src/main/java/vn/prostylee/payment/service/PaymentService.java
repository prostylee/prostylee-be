package vn.prostylee.payment.service;

import vn.prostylee.core.service.MasterDataService;
import vn.prostylee.payment.dto.response.PaymentResponse;
import vn.prostylee.payment.entity.PaymentType;

public interface PaymentService extends MasterDataService<PaymentResponse> {
    PaymentType getPaymentById(Long id);
}
