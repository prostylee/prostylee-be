package vn.prostylee.notification.dto.mail;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SimpleMailInfo {

    @Setter(AccessLevel.NONE)
    private final List<String> to = new ArrayList<>();

    private String subject;

    private String content;

    public SimpleMailInfo(@NonNull String to, @NonNull String subject, String content) {
        this.addTo(to);
        this.setSubject(subject);
        if (content != null) {
            this.setContent(content);
        }
    }

    public void addTo(String mail) {
        to.add(mail);
    }

    public void addTos(List<String> mails) {
        to.addAll(mails);
    }

    public String[] getTo() {
        return to.toArray(new String[0]);
    }

}
