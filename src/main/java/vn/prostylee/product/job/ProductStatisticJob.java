package vn.prostylee.product.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import vn.prostylee.comment.service.CommentAggregationService;
import vn.prostylee.core.constant.TargetType;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.order.dto.response.ProductSoldCountResponse;
import vn.prostylee.order.service.OrderService;
import vn.prostylee.product.constant.ProductStatus;
import vn.prostylee.product.dto.filter.ProductIdFilter;
import vn.prostylee.product.entity.ProductStatistic;
import vn.prostylee.product.repository.ProductStatisticRepository;
import vn.prostylee.product.service.ProductService;
import vn.prostylee.useractivity.dto.response.LikeCountResponse;
import vn.prostylee.useractivity.dto.response.RatingResultCountResponse;
import vn.prostylee.useractivity.dto.response.ReviewCountResponse;
import vn.prostylee.useractivity.service.UserLikeService;
import vn.prostylee.useractivity.service.UserRatingService;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    @Autowired
    private UserLikeService userLikeService;

    @Autowired
    private CommentAggregationService commentAggregationService;

    @Autowired
    private ProductService productService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        log.debug("ProductStatisticJob executing ...");
        countNumberOfSold();
        countNumberOfLike();
        countResultOfRating();
        countNumberOfReview();
//        countNumberOfComment();
    }

    private void countNumberOfSold() {
        int page = 0;
        Page<ProductSoldCountResponse> pageProductSold = orderService.countProductSold(new PagingParam(page, LIMIT));
        log.debug("totalPages={}, totalElements={}, productSoldSize={}", pageProductSold.getTotalPages(), pageProductSold.getTotalElements(), pageProductSold.getNumberOfElements());
        while (pageProductSold.getNumberOfElements() > 0) {
            upsertProductSoldStatistic(pageProductSold.getContent());
            page++;
            pageProductSold = orderService.countProductSold(new PagingParam(page, LIMIT));
            log.debug("totalPages={}, totalElements={}, productSoldSize={}", pageProductSold.getTotalPages(), pageProductSold.getTotalElements(), pageProductSold.getNumberOfElements());
        }
    }

    private void countNumberOfLike() {
        int page = 0;
        Page<LikeCountResponse> likeCountResponsePage = userLikeService.countNumberLike(new PagingParam(page, LIMIT), TargetType.PRODUCT);
        log.debug("totalPages={}, totalElements={}, postLikeSize={}", likeCountResponsePage.getTotalPages(), likeCountResponsePage.getTotalElements(), likeCountResponsePage.getNumberOfElements());
        while (likeCountResponsePage.getNumberOfElements() > 0) {
            upsertLikeStatistic(likeCountResponsePage.getContent());
            page++;
            likeCountResponsePage = userLikeService.countNumberLike(new PagingParam(page, LIMIT), TargetType.PRODUCT);
            log.debug("totalPages={}, totalElements={}, postLikeSize={}", likeCountResponsePage.getTotalPages(), likeCountResponsePage.getTotalElements(), likeCountResponsePage.getNumberOfElements());
        }
    }

    private void countNumberOfComment() {
        int page = 0;
        ProductIdFilter productIdFilter = ProductIdFilter.builder()
                .productStatus(ProductStatus.PUBLISHED)
                .build();
        Page<Long> productIdsPage = getProductIds(productIdFilter, page);
        while (productIdsPage.getNumberOfElements() > 0) {
            upsertCommentStatistic(productIdsPage.getContent());
            page++;
            productIdsPage = getProductIds(productIdFilter, page);
        }
    }

    private Page<Long> getProductIds(ProductIdFilter productIdFilter, int page) {
        productIdFilter.setLimit(LIMIT);
        productIdFilter.setPage(page);
        Page<Long> productIdsPage = productService.getProductIds(productIdFilter);
        log.debug("totalPages={}, totalElements={}, postLikeSize={}", productIdsPage.getTotalPages(), productIdsPage.getTotalElements(), productIdsPage.getNumberOfElements());
        return productIdsPage;
    }

    private void upsertCommentStatistic(List<Long> productIds) {
        Map<Long, Long> mapProductCount = productIds.stream()
                .collect(Collectors.toMap(Function.identity(), id -> commentAggregationService.count(id, TargetType.PRODUCT)));
        log.debug("mapProductCount={}", mapProductCount);
        upsertProductStatistic(mapProductCount, "numberOfComment");
    }

    private void countResultOfRating() {
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

    private void countNumberOfReview() {
        int page = 0;
        Page<ReviewCountResponse> pageRatingResult = userRatingService.countNumberReview(new PagingParam(page, LIMIT));
        log.debug("totalPages={}, totalElements={}, productSoldSize={}", pageRatingResult.getTotalPages(), pageRatingResult.getTotalElements(), pageRatingResult.getNumberOfElements());
        while (pageRatingResult.getNumberOfElements() > 0) {
            upsertReviewStatistic(pageRatingResult.getContent());
            page++;
            pageRatingResult = userRatingService.countNumberReview(new PagingParam(page, LIMIT));
            log.debug("totalPages={}, totalElements={}, productSoldSize={}", pageRatingResult.getTotalPages(), pageRatingResult.getTotalElements(), pageRatingResult.getNumberOfElements());
        }
    }

    private void upsertProductSoldStatistic(List<ProductSoldCountResponse> productSoldCountResponses) {
        final Map<Long, Long> mapProductCount = productSoldCountResponses.stream()
                .collect(Collectors.toMap(ProductSoldCountResponse::getProductId, ProductSoldCountResponse::getCount));
        upsertProductStatistic(mapProductCount, "numberOfSold");
    }

    private void upsertRatingStatistic(List<RatingResultCountResponse> ratingResultCountResponses) {
        final Map<Long, Double> mapProductCount = ratingResultCountResponses.stream()
                .collect(Collectors.toMap(RatingResultCountResponse::getProductId, RatingResultCountResponse::getCount));
        upsertProductStatistic(mapProductCount, "resultOfRating");
    }

    private void upsertReviewStatistic(List<ReviewCountResponse> reviewCountResponses) {
        final Map<Long, Long> mapProductCount = reviewCountResponses.stream()
                .collect(Collectors.toMap(ReviewCountResponse::getProductId, ReviewCountResponse::getCount));
        upsertProductStatistic(mapProductCount, "numberOfReview");
    }

    private void upsertLikeStatistic(List<LikeCountResponse> likeCountResponses) {
        final Map<Long, Long> mapProductCount = likeCountResponses.stream()
                .collect(Collectors.toMap(LikeCountResponse::getId, LikeCountResponse::getCount));
        upsertProductStatistic(mapProductCount, "numberOfLike");
    }

    /**
     * Insert or Update Product Statistic
     *
     * @param mapCount key is a product Id, value is a count value of fieldName
     * @param fieldName The field name in ProductStatistic entity
     */
    private void upsertProductStatistic(Map<Long, ? extends Number> mapCount, String fieldName) {
        List<ProductStatistic> statistics = productStatisticRepository.findByProductIds(mapCount.keySet());
        statistics.forEach(productStatistic -> {
            setFieldValue(productStatistic, fieldName, mapCount.get(productStatistic.getId()));
            mapCount.remove(productStatistic.getId());
        });

        List<ProductStatistic> adds = mapCount.keySet()
                .stream()
                .map(productId -> {
                    ProductStatistic productStatistic = ProductStatistic.builder().id(productId).build();
                    setFieldValue(productStatistic, fieldName, mapCount.get(productId));
                    return productStatistic;
                })
                .collect(Collectors.toList());

        statistics.addAll(adds);

        productStatisticRepository.saveAll(statistics);
    }

    private <T extends Number> void setFieldValue(ProductStatistic productStatistic, String fieldName, T value) {
        try {
            FieldUtils.writeDeclaredField(productStatistic, fieldName, value, true);
        } catch (IllegalAccessException e) {
            log.warn("Could not write value {} for field {} of object {}", value, fieldName, productStatistic, e);
        }
    }
}
