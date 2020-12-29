package vn.prostylee.extension;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SmtpServerExtension {

    @Value("${spring.mail.port:2525}")
    private int port;

    @Value("${spring.mail.username:test}")
    private String username;

    @Value("${spring.mail.password:secret}")
    private String password;

    @Getter
    private GreenMail smtpServer;

    @BeforeAll
    public void beforeAll() {
        log.info("Starting GreenMail SMTP server ...");

        smtpServer = new GreenMail(new ServerSetup(port, null, ServerSetup.PROTOCOL_SMTP));
        smtpServer.start();

        log.info("Started GreenMail SMTP server successfully");
    }

    @BeforeEach
    public void beforeEach() {
        smtpServer.reset();
        smtpServer.setUser("to@localhost", username, password);
    }

    @AfterAll
    public void afterAll() {
        log.info("Stopping GreenMail SMTP server ...");

        smtpServer.stop();

        log.info("Stopped GreenMail SMTP server successfully");
    }
}