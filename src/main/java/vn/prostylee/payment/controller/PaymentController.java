package vn.prostylee.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.MasterDataController;
import vn.prostylee.payment.dto.response.PaymentResponse;
import vn.prostylee.payment.service.PaymentService;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/payments")
public class PaymentController extends MasterDataController<PaymentResponse> {

    private PaymentService service;

    @Autowired
    public PaymentController(PaymentService service) {
        super(service);
        this.service = service;
    }
}
