package vn.prostylee.auth.token.verifier;

/**
 * Check whether the token is valid
 */
public interface TokenVerifier {

    boolean verify(String token);
}
