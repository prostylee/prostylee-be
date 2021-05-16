package vn.prostylee.order.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.configuration.monitor.workflow.MutationEvent;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.notification.constant.EmailTemplateType;
import vn.prostylee.notification.constant.PushNotificationTemplateType;
import vn.prostylee.notification.event.email.EmailEvent;
import vn.prostylee.notification.event.email.EmailEventDto;
import vn.prostylee.notification.event.push.PushNotificationEvent;
import vn.prostylee.notification.event.push.PushNotificationEventDto;
import vn.prostylee.order.dto.response.OrderResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderMutationEventHandler implements MutationEvent {

    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;

    @Override
    public <T, R> void onPostCreate(T request, R response) {
        log.debug("OrderMutationEventHandler request={}, response={}", request, response);
        if (!(response instanceof OrderResponse)) {
            return;
        }

        OrderResponse orderResponse = BeanUtil.copyProperties(response, OrderResponse.class);
        Optional<UserResponse> userResponse = userService.fetchById(orderResponse.getBuyerId());
        if (orderResponse.getBuyer() == null && userResponse.isPresent()) {
            orderResponse.setBuyer(userResponse.get());
        }

        userResponse.map(UserResponse::getEmail).ifPresent(email -> sendEmailNewOrderToCustomer(email, orderResponse));
        sendNotificationNewOrderToStoreOwner(orderResponse);
    }

    private void sendEmailNewOrderToCustomer(String email, OrderResponse order) {
        EmailEventDto<?> eventDto = EmailEventDto.builder()
                .emailTemplateType(EmailTemplateType.CUSTOMER_NEW_ORDER)
                .emails(Collections.singletonList(email))
                .data(order)
                .build();

        eventPublisher.publishEvent(new EmailEvent(eventDto));
    }

    private void sendNotificationNewOrderToStoreOwner(OrderResponse order) {
        Map<String, Object> pushData = new HashMap<>();
        pushData.put("orderId", order.getId());
        pushData.put("orderCode", order.getCode());
        pushData.put("buyerId", order.getBuyerId());

        PushNotificationEventDto<?> eventDto = PushNotificationEventDto.builder()
                .templateType(PushNotificationTemplateType.STORE_NEW_ORDER)
                .storeIds(order.getOrderDetails().stream().map(detail -> detail.getStore().getId()).collect(Collectors.toList()))
                .pushData(pushData)
                .fillData(order)
                .build();
        eventPublisher.publishEvent(new PushNotificationEvent(eventDto));
    }
}
