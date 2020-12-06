package vn.prostylee.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.constant.SocialProviderType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthenticationServiceFactory {

    private static final Map<SocialProviderType, AuthenticationService> myServiceCache = new HashMap<>();

    @Autowired
    public void initMyServiceCache(List<AuthenticationService> services) {
        for(AuthenticationService service : services) {
            myServiceCache.put(service.getProviderType(), service);
        }
    }

    public static AuthenticationService getService(SocialProviderType type) {
        AuthenticationService service = myServiceCache.get(type);
        if(service == null) throw new RuntimeException("Unknown service type: " + type);
        return service;
    }
}
