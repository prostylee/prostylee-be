package vn.prostylee.core.configuration.monitor.userbehavior;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.utils.FieldUtils;
import vn.prostylee.useractivity.dto.request.UserTrackingRequest;
import vn.prostylee.useractivity.service.UserTrackingService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * The class to support get the information to keep track the user action.
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class UserTrackingAspect {
    public static final String KEYWORD = "keyword";
    public static final String CATEGORY_ID = "categoryId";
    public static final String PRODUCT_ID = "productId";
    public static final String STORE_ID = "storeId";

    public static final String PRODUCT_PATH = "/products";
    public static final String STORE_PATH = "/stores";
    public static final String CATEGORY_PATH = "/categories";

    private final UserTrackingService userTrackingService;
    private final HttpServletRequest request;

    @Before("@annotation(vn.prostylee.core.configuration.monitor.userbehavior.UserBehaviorTracking)")
    public void trackingUserBehavior(JoinPoint joinPoint) {
        try {
            List<Object> args = Arrays.asList(joinPoint.getArgs());
            if (CollectionUtils.isEmpty(args)) {
                return;
            }

            UserTrackingRequest requestDto = null;

            Object firstArg = args.get(0);
            if (firstArg instanceof BaseFilter) {
                requestDto = buildUserTrackingRequest((BaseFilter) firstArg);
            } else if (firstArg instanceof Long) {
                String requestUrl = request.getRequestURL().toString();
                requestDto = buildUserTrackingRequest(requestUrl, (Long) firstArg);
            }

            if (requestDto != null) {
                userTrackingService.storeTracking(requestDto);
            }
            log.debug("UserTracking aspect: signature={}, userTrackingRequest={}", joinPoint.getSignature().getName(), requestDto);
        } catch (Exception e) {
            log.error("Could not track user activity", e);
        }
    }

    private UserTrackingRequest buildUserTrackingRequest(String requestUrl, Long requestId) {
        if (requestUrl.contains(CATEGORY_PATH)) {
            return UserTrackingRequest.builder().path(requestUrl).categoryId(requestId).build();
        }

        if (requestUrl.contains(STORE_PATH)) {
            return UserTrackingRequest.builder().path(requestUrl).storeId(requestId).build();
        }

        if (requestUrl.contains(PRODUCT_PATH)) {
            return UserTrackingRequest.builder().path(requestUrl).productId(requestId).build();
        }

        return null;
    }

    private UserTrackingRequest buildUserTrackingRequest(BaseFilter filter) {
        Long categoryId = FieldUtils.readField(Long.class, filter, CATEGORY_ID);
        Long productId = FieldUtils.readField(Long.class, filter, PRODUCT_ID);
        Long storeId = FieldUtils.readField(Long.class, filter, STORE_ID);
        String keyword = FieldUtils.readField(String.class, filter, KEYWORD);

        if (categoryId == null && productId == null && storeId == null && StringUtils.isBlank(keyword)) {
            return null;
        }

        return UserTrackingRequest.builder()
                .categoryId(categoryId)
                .productId(productId)
                .storeId(storeId)
                .path(request.getRequestURL().toString())
                .searchKeyword(keyword)
                .build();
    }
}

