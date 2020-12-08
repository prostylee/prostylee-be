package vn.prostylee.notification.dto;

import lombok.Data;

import java.util.List;

@Data
public class MailData {

    private String name;

    private String subscriptionDate;

    private List<String> hobbies;

    private String imageResourceName;
}
