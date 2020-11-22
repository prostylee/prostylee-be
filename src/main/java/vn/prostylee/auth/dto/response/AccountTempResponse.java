package vn.prostylee.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountTempResponse {

    private String username;

    private String passwordInPlainText;

    private Date expiredAt;

    private Integer expiredInMinutes;
}
