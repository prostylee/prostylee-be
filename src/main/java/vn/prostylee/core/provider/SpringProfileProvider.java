package vn.prostylee.core.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SpringProfileProvider {

    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    public boolean isProdEnvironment() {
        if (activeProfiles == null) {
            return false;
        }
        String[] profiles = activeProfiles.split(",");
        return Arrays.asList(profiles).contains("prod");
    }
}
