package vn.prostylee.core.utils;

import vn.prostylee.core.exception.ApplicationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncrytedPasswordUtils {
	
	private EncrytedPasswordUtils() {
		super();
	}
	
	/*
	 * Encrypt Password with BCryptPasswordEncoder
	 */
	public static String encryptPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}

	public static boolean isMatched(String rawPassword, String encodedPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(rawPassword, encodedPassword);
	}

	public static String md5(String data) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.update(data.getBytes());
			byte[] digest = messageDigest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(Integer.toHexString(b & 0xff));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new ApplicationException("Can not MD5 [" + data + "]");
		}
	}

}
