package vn.prostylee.core.configuration.monitor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import vn.prostylee.useractivity.dto.request.UserTrackingRequest;
import vn.prostylee.useractivity.service.UserTrackingService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * The class to support get the information to keep track the user action.
 */
@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.enabled:true}")
@RequiredArgsConstructor
public class UserTrackingAspect {
    public static final String KEYWORD = "keyword";
    public static final String CATEGORY_ID = "categoryId";
    public static final String PRODUCT_ID = "productId";
    public static final String STORE_ID = "storeId";
    public static final String ZERO = "0";

    private final HttpServletRequest request;

    private final UserTrackingService userTrackingService;

    @Before("@annotation(vn.prostylee.core.configuration.monitor.annotation.TrackUserBehavior)")
    public void executionTime(JoinPoint joinPoint) {

        Long  categoryId = Long.valueOf(Optional.ofNullable(getParameterBy(CATEGORY_ID)).orElse(ZERO));
        Long  productId = Long.valueOf(Optional.ofNullable(getParameterBy(PRODUCT_ID)).orElse(ZERO));
        Long  storeId = Long.valueOf(Optional.ofNullable(getParameterBy(STORE_ID)).orElse(ZERO));

        UserTrackingRequest requestDto = UserTrackingRequest.builder()
                .categoryId(categoryId)
                .productId(productId)
                .storeId(storeId)
                .path(request.getRequestURL().toString())
                .searchKeyword(getParameterBy(KEYWORD))
                .build();

        userTrackingService.storeTracking(requestDto);
        log.debug("UserTracking aspect: " + joinPoint.getSignature().getName());
    }

    private String getParameterBy(String paramKey) {
        return request.getParameter(paramKey);
    }

}

