package vn.prostylee.product.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.order.dto.response.ProductSoldCountResponse;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductStatistic;
import vn.prostylee.product.repository.ProductStatisticRepository;
import vn.prostylee.useractivity.dto.response.RatingResultCountResponse;
import vn.prostylee.useractivity.service.UserRatingService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@DisallowConcurrentExecution
// This annotation tells Quartz that a given Job definition (that is, a JobDetail instance) does not run concurrently.
public class ProductStatisticJob extends QuartzJobBean {

    public static final int LIMIT = 50;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductStatisticRepository productStatisticRepository;

    @Autowired
    private UserRatingService userRatingService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.debug("ProductStatisticJob executing ...");
        countNumberOfSold();
        countNumberOfLike();
        countNumberOfComment();
        countResultOfRating();
    }

    private void countNumberOfSold() {
        int page = 0;
        Page<ProductSoldCountResponse> pageProductSold = orderService.countProductSold(new PagingParam(page, LIMIT));
        log.debug("totalPages={}, totalElements={}, productSoldSize={}", pageProductSold.getTotalPages(), pageProductSold.getTotalElements(), pageProductSold.getNumberOfElements());
        while (pageProductSold.getNumberOfElements() > 0) {
            upsertProductStatistic(pageProductSold.getContent());
            page++;
            pageProductSold = orderService.countProductSold(new PagingParam(page, LIMIT));
            log.debug("totalPages={}, totalElements={}, productSoldSize={}", pageProductSold.getTotalPages(), pageProductSold.getTotalElements(), pageProductSold.getNumberOfElements());
        }
    }

    private void countNumberOfLike() {
        // TODO
    }

    private void countNumberOfComment() {
        // TODO
    }

    private void countResultOfRating(){
        int page = 0;
        Page<RatingResultCountResponse> pageRatingResult = userRatingService.countRatingResult(new PagingParam(page, LIMIT));
        log.debug("totalPages={}, totalElements={}, productSoldSize={}", pageRatingResult.getTotalPages(), pageRatingResult.getTotalElements(), pageRatingResult.getNumberOfElements());
        while (pageRatingResult.getNumberOfElements() > 0) {
            upsertRatingStatistic(pageRatingResult.getContent());
            page++;
            pageRatingResult = userRatingService.countRatingResult(new PagingParam(page, LIMIT));
            log.debug("totalPages={}, totalElements={}, productSoldSize={}", pageRatingResult.getTotalPages(), pageRatingResult.getTotalElements(), pageRatingResult.getNumberOfElements());
        }
    }

    private void upsertProductStatistic(List<ProductSoldCountResponse> productSoldCountResponses) {
        final Map<Long, Long> mapProductCount = productSoldCountResponses.stream()
                .collect(Collectors.toMap(ProductSoldCountResponse::getProductId, ProductSoldCountResponse::getCount));

        List<ProductStatistic> statistics = productStatisticRepository.findByProductIds(mapProductCount.keySet());

        statistics.forEach(productStatistic -> {
            productStatistic.setNumberOfSold(mapProductCount.getOrDefault(productStatistic.getProduct().getId(), 0L));
            mapProductCount.remove(productStatistic.getProduct().getId());
        });

        List<ProductStatistic> adds = mapProductCount.keySet()
                .stream()
                .map(productId -> {
                    Product product = new Product(productId);
                    return ProductStatistic.builder()
                            .product(product)
                            .numberOfSold(mapProductCount.getOrDefault(productId, 0L))
                            .build();
                })
                .collect(Collectors.toList());

        statistics.addAll(adds);

        productStatisticRepository.saveAll(statistics);
    }

    private void upsertRatingStatistic(List<RatingResultCountResponse> ratingResultCountResponses) {
        final Map<Long, Double> mapProductCount = ratingResultCountResponses.stream()
                .collect(Collectors.toMap(RatingResultCountResponse::getProductId, RatingResultCountResponse::getCount));

        List<ProductStatistic> statistics = productStatisticRepository.findByProductIds(mapProductCount.keySet());

        statistics.forEach(productStatistic -> {
            productStatistic.setResultOfRating(mapProductCount.getOrDefault(productStatistic.getProduct().getId(), (double) 0L));
            mapProductCount.remove(productStatistic.getProduct().getId());
        });

        List<ProductStatistic> adds = mapProductCount.keySet()
                .stream()
                .map(productId -> {
                    Product product = new Product(productId);
                    return ProductStatistic.builder()
                            .product(product)
                            .resultOfRating(mapProductCount.getOrDefault(productId, (double) 0L))
                            .build();
                })
                .collect(Collectors.toList());

        statistics.addAll(adds);

        productStatisticRepository.saveAll(statistics);
    }
}
