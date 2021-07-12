package vn.prostylee.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.TrackingCrudController;
import vn.prostylee.order.dto.filter.BestSellerFilter;
import vn.prostylee.product.dto.filter.*;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.NewFeedResponse;
import vn.prostylee.product.dto.response.ProductForStoryResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.dto.response.ProductTabsServiceResponse;
import vn.prostylee.product.service.ProductCollectionService;
import vn.prostylee.product.service.ProductForStoryService;
import vn.prostylee.product.service.ProductOrderService;
import vn.prostylee.product.service.ProductService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/products")
public class ProductController extends TrackingCrudController<ProductRequest, ProductResponse, Long, ProductFilter> {

    private final ProductService productService;
    private final ProductForStoryService productForStoryService;
    private final ProductCollectionService productCollectionService;
    private final ProductOrderService productOrderService;

    @Autowired
    public ProductController(
            ProductService productService,
            ProductForStoryService productForStoryService,
            ProductCollectionService productCollectionService,
            ProductOrderService productOrderService) {
        super(productService);
        this.productService = productService;
        this.productForStoryService = productForStoryService;
        this.productCollectionService = productCollectionService;
        this.productOrderService = productOrderService;
    }

    @GetMapping("/new-feeds")
    public Page<ProductResponse> getNewFeeds(@Valid ProductFilter productFilter) {
        return productService.getNewFeeds(productFilter);
    }

    @GetMapping("/new-feeds/store")
    public Page<NewFeedResponse> getNewFeedsOfStore(@Valid NewFeedsFilter newFeedsFilter) {
        return productService.getNewFeedsOfStore(newFeedsFilter);
    }

    @GetMapping("/product-for-story/{productId}")
    public ProductForStoryResponse getProductForStory(@PathVariable(value = "productId") Long productId) {
        return productForStoryService.getProductForStory(productId);
    }

    @GetMapping("/{productId}/related")
    public Page<ProductResponse> getRelatedProducts(@PathVariable(value = "productId") Long productId, RelatedProductFilter relatedProductFilter) {
        return productCollectionService.getRelatedProducts(productId, relatedProductFilter);
    }

    @GetMapping("/{productId}/suggestions")
    public Page<ProductResponse> getSuggestionProductsById(@PathVariable(value = "productId") Long productId, SuggestionProductFilter suggestionProductFilter) {
        return productCollectionService.getSuggestionProducts(productId, suggestionProductFilter);
    }

    @GetMapping("/suggestions")
    public Page<ProductResponse> getSuggestionProducts(@Valid SuggestionProductFilter suggestionProductFilter) {
        return productCollectionService.getSuggestionProducts(null, suggestionProductFilter);
    }

    @GetMapping("/best-seller")
    public Page<ProductResponse> getBestSellerProducts(@Valid BestSellerFilter bestSellerFilter) {
        return productCollectionService.getBestSellerProducts(bestSellerFilter);
    }

    @GetMapping("/recent-view")
    public Page<ProductResponse> getRecentViewProducts(@Valid RecentViewProductFilter recentViewProductFilter) {
        return new PageImpl<>(productService.getRecentViewProducts(recentViewProductFilter));
    }

    @GetMapping("/me/purchased")
    public Page<ProductResponse> getPurchasedProductsByMe(@Valid PurchasedProductFilter purchasedProductFilter) {
        return productOrderService.getPurchasedProductsByMe(purchasedProductFilter);
    }

    @GetMapping("/{userId}/purchased")
    public Page<ProductResponse> getPurchasedProductsByMe(@PathVariable(value = "userId") Long userId, @Valid PurchasedProductFilter purchasedProductFilter) {
        return productOrderService.getPurchasedProductsByUserId(userId, purchasedProductFilter);
    }

    @GetMapping("/allTabs")
    public List<ProductTabsServiceResponse> getListAllService(){
        List<ProductTabsServiceResponse> productTabsServiceResponses = new ArrayList<>();
        ProductTabsServiceResponse tabAll = ProductTabsServiceResponse.builder()
                .tabName("Tất cả")
                .apiUrl(ApiVersion.API_V1 + "/products")
                .apiMethod("Get").build();
        productTabsServiceResponses.add(tabAll);

        ProductTabsServiceResponse tabBestSeller = ProductTabsServiceResponse.builder()
                .tabName("Best-seller")
                .apiUrl(ApiVersion.API_V1 + "/products/best-seller")
                .apiMethod("Get").build();
        productTabsServiceResponses.add(tabBestSeller);

        ProductTabsServiceResponse tabSuggestions = ProductTabsServiceResponse.builder()
                .tabName("Đề xuất")
                .apiUrl(ApiVersion.API_V1 + "/products/suggestions")
                .apiMethod("Get").build();
        productTabsServiceResponses.add(tabSuggestions);

        return productTabsServiceResponses;
    }
}
