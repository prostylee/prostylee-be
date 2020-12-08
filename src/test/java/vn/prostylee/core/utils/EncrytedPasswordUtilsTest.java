package vn.prostylee.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vn.prostylee.core.utils.EncrytedPasswordUtils;

@Slf4j
public class EncrytedPasswordUtilsTest {

    @Test
    public void encrytePasswordTest() {
        log.info("pwd " + EncrytedPasswordUtils.encryptPassword("12345678"));
        Assertions.assertNotNull(EncrytedPasswordUtils.encryptPassword("12345678"));
    }
}
