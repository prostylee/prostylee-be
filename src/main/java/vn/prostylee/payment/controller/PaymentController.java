package vn.prostylee.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.payment.dto.filter.PaymentFilter;
import vn.prostylee.payment.dto.request.PaymentRequest;
import vn.prostylee.payment.dto.response.PaymentResponse;
import vn.prostylee.payment.service.PaymentService;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/payments")
public class PaymentController extends CrudController<PaymentRequest, PaymentResponse, Long, PaymentFilter> {

    private PaymentService service;

    @Autowired
    public PaymentController(PaymentService service) {
        super(service);
        this.service = service;
    }
}
