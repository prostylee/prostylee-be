package vn.prostylee.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.response.ProductPriceRangeResponse;
import vn.prostylee.product.dto.response.ProductPriceResponse;
import vn.prostylee.product.service.ProductPriceService;
import vn.prostylee.product.service.ProductService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/product-price")
public class ProductPriceController extends CrudController<ProductPriceRequest, ProductPriceResponse, Long, ProductFilter> {

    private final ProductPriceService productPriceService;

    @Autowired
    public ProductPriceController(ProductPriceService productPriceService) {
        super(productPriceService);
        this.productPriceService = productPriceService;
    }

    @GetMapping("/priceRange")
    public ProductPriceRangeResponse getProductPriceRange() {
        return productPriceService.getProductPriceRange();
    }
}
