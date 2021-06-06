package vn.prostylee.product.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.product.dto.filter.BrandFilter;
import vn.prostylee.product.dto.request.BrandRequest;
import vn.prostylee.product.dto.response.BrandResponse;
import vn.prostylee.product.service.BrandService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/brands")
public class BrandController extends CrudController<BrandRequest, BrandResponse, Long, BrandFilter> {

    public BrandController(BrandService brandService) {
        super(brandService);
    }
}
