package vn.prostylee.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.product.dto.filter.AttributeFilter;
import vn.prostylee.product.dto.request.AttributeRequest;
import vn.prostylee.product.dto.response.AttributeResponse;
import vn.prostylee.product.service.AttributeService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/attributes")
public class AttributeController extends CrudController<AttributeRequest, AttributeResponse, Long, AttributeFilter> {

    public AttributeController(AttributeService attributeService) {
        super(attributeService);
    }
}
