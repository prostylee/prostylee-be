package vn.prostylee.product.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.filter.NewFeedsFilter;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.filter.RecentViewProductFilter;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.NewFeedResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.dto.response.ProductResponseLite;
import vn.prostylee.product.entity.Product;

import java.util.List;

public interface ProductService extends CrudService<ProductRequest, ProductResponse, Long> {

    long countTotalProductByUser(Long userId);

    Product getById(Long id);

    List<ProductResponse> getRecentViewProducts(RecentViewProductFilter recentViewProductFilter);

    Page<ProductResponseLite> getProductsForListStore(BaseFilter baseFilter);

    List<ProductResponseLite> findByIds(List<Long> productIds);

    Page<NewFeedResponse> getNewFeeds(NewFeedsFilter newFeedsFilter);
}
