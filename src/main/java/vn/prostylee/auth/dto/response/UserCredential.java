package vn.prostylee.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCredential {

    private Long id;

    private String sub;

    private String fullName;

    private String username;

    private String phoneNumber;

    private Character gender;

    private List<String> roles;

    private List<FeatureResponse> features;
}
