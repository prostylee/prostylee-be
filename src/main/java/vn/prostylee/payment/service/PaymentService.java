package vn.prostylee.payment.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.payment.dto.request.PaymentRequest;
import vn.prostylee.payment.dto.response.PaymentResponse;

public interface PaymentService extends CrudService<PaymentRequest, PaymentResponse, Long> {
}
