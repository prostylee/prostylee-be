package vn.prostylee.auth.configure.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.configure.properties.AwsCognitoProperties;
import vn.prostylee.auth.dto.AwsCognitoJwtAuthentication;
import vn.prostylee.auth.dto.response.UserCredential;
import vn.prostylee.auth.exception.AuthenticationException;
import vn.prostylee.auth.token.extractor.TokenExtractor;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Verify AWS Cognito Token
 *
 * @see https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-tokens-verifying-a-jwt.html
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AwsCognitoJwtAuthenticationFilter extends AuthOncePerRequestFilter {

    private static final String userNameField = "cognito:username";
    private static final String groupsField = "cognito:groups";
    private static final String phoneNumber = "phone_number";
    private static final String fullName = "name";

    private final TokenExtractor tokenExtractor;

    private final ConfigurableJWTProcessor configurableJWTProcessor;

    private final AwsCognitoProperties awsCognitoProperties;

    @Override
    boolean setAuthIfTokenValid(HttpServletRequest request) {
        try {
            String jwt = tokenExtractor.extract(request);
            if (StringUtils.equals("OPEN-ID", request.getHeader("X-PS-Authorization-Type"))
                    && StringUtils.isNotBlank(jwt)) {
                JWTClaimsSet claimsSet = configurableJWTProcessor.process(jwt, null);
                if (isIssuedCorrectly(claimsSet) && isIdToken(claimsSet)) {
                    List<String> groups = extractUserGroups(claimsSet);

                    List<SimpleGrantedAuthority> grantedAuthorities = convertToGrantedAuthorities(groups);

                    String username = (String) claimsSet.getClaim(userNameField);
                    UserCredential user = UserCredential.builder()
                            .id(0L) // TODO change user id from Long to String
                            .username(username)
                            .fullName(username) // TODO set more fields
                            .roles(groups)
                            .features(Collections.emptyList())
                            .build();
                    Authentication auth = new AwsCognitoJwtAuthentication(user, claimsSet, grantedAuthorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    return true;
                }
            }
        } catch (ParseException | JOSEException | BadJOSEException ex) {
            throw new AuthenticationException(ex.getMessage(), ex);
        }
        return false;
    }

    private List<String> extractUserGroups(JWTClaimsSet claimsSet) {
        return Optional.of(claimsSet.getClaims())
                .map(m -> m.get(groupsField))
                .map(List.class::cast)
                .stream()
                .filter(Objects::nonNull)
                .map(obj -> obj.toString().toUpperCase())
                .collect(Collectors.toList());
    }

    private List<SimpleGrantedAuthority> convertToGrantedAuthorities(List<String> groups) {
        return groups.stream()
                .map(group -> new SimpleGrantedAuthority("ROLE_" + group))
                .collect(Collectors.toList());
    }

    private boolean isIssuedCorrectly(JWTClaimsSet claimsSet) {
        return claimsSet.getIssuer().equals(awsCognitoProperties.getIssuer());
    }

    private boolean isIdToken(JWTClaimsSet claimsSet) {
        String tokenUse = (String) claimsSet.getClaim("token_use");
        return StringUtils.equalsAny(tokenUse, "id", "access");
    }
}