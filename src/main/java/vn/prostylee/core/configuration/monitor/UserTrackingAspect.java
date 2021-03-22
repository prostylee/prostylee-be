package vn.prostylee.core.configuration.monitor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import vn.prostylee.core.utils.FieldUtils;
import vn.prostylee.useractivity.dto.request.UserTrackingRequest;
import vn.prostylee.useractivity.service.UserTrackingService;

import javax.servlet.http.HttpServletRequest;

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

    private final UserTrackingService userTrackingService;
    private final HttpServletRequest request;

    @Before("@annotation(vn.prostylee.core.configuration.monitor.annotation.UserBehaviorTracking)")
    public void trackingUserBehavior(JoinPoint joinPoint) {
        Object filter = joinPoint.getArgs()[0];
        Long categoryId =  FieldUtils.readField(Long.class , filter, CATEGORY_ID);
        Long productId = FieldUtils.readField(Long.class , filter, PRODUCT_ID);
        Long storeId  = FieldUtils.readField(Long.class , filter, STORE_ID);
        String keyword = FieldUtils.readField(String.class, filter, KEYWORD);

        UserTrackingRequest requestDto = UserTrackingRequest.builder()
                .categoryId(categoryId)
                .productId(productId)
                .storeId(storeId)
                .path(request.getRequestURL().toString())
                .searchKeyword(keyword)
                .build();

        userTrackingService.storeTracking(requestDto);
        log.debug("UserTracking aspect: " + joinPoint.getSignature().getName());
    }
}

