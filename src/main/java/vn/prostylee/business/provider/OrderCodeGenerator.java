package vn.prostylee.business.provider;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderCodeGenerator {

    @Value("${app.order-code.length}")
    private Integer orderCodeLength;

    public String generate() {
        String prefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String randomString = RandomStringUtils.randomAlphanumeric(orderCodeLength).toUpperCase();
        return prefix + "-" + randomString;
    }
}
