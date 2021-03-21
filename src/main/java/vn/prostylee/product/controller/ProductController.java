package vn.prostylee.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.core.controller.TrackingCrudController;
import vn.prostylee.product.constant.NewFeedType;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductForStoryResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.service.ProductForStoryService;
import vn.prostylee.product.service.ProductService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/products")
public class ProductController extends TrackingCrudController<ProductRequest, ProductResponse, Long, ProductFilter> {

    private final ProductService productService;
    private final ProductForStoryService productForStoryService;

    @Autowired
    public ProductController(ProductService productService, ProductForStoryService productForStoryService) {
        super(productService);
        this.productService = productService;
        this.productForStoryService = productForStoryService;
    }

    @GetMapping("/new-feeds")
    public Page<ProductResponse> getNewFeeds(ProductFilter productFilter) {
        if (productFilter.getNewFeedType() == null) {
            productFilter.setNewFeedType(NewFeedType.STORE);
        }
        return productService.findAll(productFilter);
    }

    @GetMapping("/product-for-story/{productId}")
    public ProductForStoryResponse getProductForStory(@PathVariable(value = "productId") Long productId) {
        return productForStoryService.getProductForStory(productId);
    }

}
