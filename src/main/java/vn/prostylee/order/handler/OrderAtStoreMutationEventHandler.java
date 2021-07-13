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
import vn.prostylee.order.dto.request.OrderAtStoreRequest;
import vn.prostylee.order.dto.response.OrderAtStoreResponse;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.store.entity.Store;
import vn.prostylee.store.service.StoreService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderAtStoreMutationEventHandler implements MutationEvent {

    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;
    private final StoreService storeService;

    @Override
    public <T, R> void onPostCreate(T request, R response) {
        log.debug("OrderMutationEventHandler request={}, response={}", request, response);
        if (!(response instanceof OrderResponse)) {
            return;
        }

        OrderAtStoreResponse orderAtStoreResponse = BeanUtil.copyProperties(response, OrderAtStoreResponse.class);
        Store store = storeService.getById(orderAtStoreResponse.getStoreResponseLite().getId());
        Optional<UserResponse> userResponse = userService.fetchById(store.getOwnerId());

        sendNotificationNewOrderToStoreOwner(orderAtStoreResponse, userResponse.get().getId());
    }

    private void sendNotificationNewOrderToStoreOwner(OrderAtStoreResponse order, Long storeOwnerId) {
        // TODO
    }
}
