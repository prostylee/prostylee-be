package vn.prostylee.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vn.prostylee.IntegrationTest;
import vn.prostylee.useractivity.dto.request.UserTrackingRequest;
import vn.prostylee.useractivity.dto.response.UserTrackingResponse;
import vn.prostylee.useractivity.service.UserTrackingService;

@IntegrationTest
public class UserTrackingServiceIT {

    @Autowired
    protected WebApplicationContext webAppContext;

    @Autowired
    private UserTrackingService service;

    @BeforeEach
    public void setup(){
        MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void testTracking() {
        UserTrackingRequest request = UserTrackingRequest.builder()
                .productId(1L)
                .storeId(2L)
                .categoryId(3L)
                .path("http://localhost:8090/api/v1/shippings/methods")
                .searchKeyword("Iphone 12 Pro max")
                .build();

        UserTrackingResponse responses = service.storeTracking(request);

        Assertions.assertEquals(request.getCategoryId(), responses.getCategoryId());
        Assertions.assertEquals(request.getStoreId(), responses.getStoreId());
        Assertions.assertEquals(request.getProductId(), responses.getProductId());
        Assertions.assertEquals(request.getSearchKeyword(), responses.getSearchKeyword());
        Assertions.assertEquals(request.getPath(), responses.getPath());
    }
}
