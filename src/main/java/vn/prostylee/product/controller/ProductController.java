package vn.prostylee.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.service.ProductService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/products")
public class ProductController extends CrudController<ProductRequest, ProductResponse, Long, ProductFilter> {

    @Autowired
    public ProductController(ProductService productService) {
        super(productService);
    }
}
