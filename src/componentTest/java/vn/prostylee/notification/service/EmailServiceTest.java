package vn.prostylee.notification.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vn.prostylee.ComponentTest;
import vn.prostylee.notification.dto.MailData;
import vn.prostylee.notification.dto.mail.MailInfo;
import vn.prostylee.notification.dto.mail.SimpleMailInfo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

@ComponentTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Value("classpath:images/logo.png")
    private Resource resourceFile;

    @Autowired
    protected WebApplicationContext webAppContext;

    @BeforeEach
    public void setup() throws Exception {
        MockMvcBuilders.webAppContextSetup(webAppContext).build();// Standalone context
    }

    @Test
    public void sendSimpleEmail() throws IOException {
        SimpleMailInfo mailInfo = new SimpleMailInfo();
        mailInfo.addTo("gpcodervn@gmail.com");
        mailInfo.setContent("This is an email content");
        mailInfo.setSubject("Test send mail from prostylee system");
        boolean result = emailService.send(mailInfo);
        Assertions.assertTrue(result);
    }

    @Test
    public void sendMailWithAttachment() throws IOException {
        MailData data = new MailData();
        data.setName("Giang Phan");
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        data.setSubscriptionDate(df.format(new Date()));
        data.setHobbies(Arrays.asList("Cinema", "Sports", "Music"));
        data.setImageResourceName(resourceFile.getFile().getName());

        MailInfo mailInfo = new MailInfo();
        mailInfo.setFrom("ptgiang56@gmail.com");
        mailInfo.setReplyTo("ptgiang56it@gmail.com");
        mailInfo.addTo("gpcodervn@gmail.com");
        mailInfo.setContent("This is an email content");
        mailInfo.setSubject("Test send mail from prostylee system");
        mailInfo.addAttachment(resourceFile.getFile().getName(), resourceFile.getFile(), false, true);
        mailInfo.setHtml(true);
        boolean result = emailService.send(mailInfo, "html/mail-template-forgot-password.tpl.html", data);
        Assertions.assertTrue(result);
    }

}
