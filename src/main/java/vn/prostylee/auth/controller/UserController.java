package vn.prostylee.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.auth.dto.filter.UserFilter;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.order.dto.filter.OrderFilter;
import vn.prostylee.order.dto.response.OrderResponse;
import vn.prostylee.order.service.OrderService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/users")
public class UserController extends CrudController<UserRequest, UserResponse, Long, UserFilter> {

    public final UserService userService;

    public final OrderService orderService;

    private final AuthenticatedProvider authenticatedProvider;

    @Autowired
    public UserController(UserService userService,
                          OrderService orderService,
                          AuthenticatedProvider authenticatedProvider) {
        super(userService);
        this.userService = userService;
        this.orderService = orderService;
        this.authenticatedProvider = authenticatedProvider;
    }

    @GetMapping("/status")
    public Page<OrderResponse> getOrdersByLoggedUser(OrderFilter orderFilter) {
        orderFilter.setLoggedInUser(authenticatedProvider.getUserIdValue());
        return orderService.findAll(orderFilter);
    }
}
