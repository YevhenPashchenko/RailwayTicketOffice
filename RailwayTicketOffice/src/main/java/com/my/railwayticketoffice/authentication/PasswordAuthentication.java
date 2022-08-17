package com.my.railwayticketoffice.authentication;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Hash passwords for storage, and check passwords on authentication
 *
 * @author Yevhen Pashchenko
 */
public final class PasswordAuthentication {

    private static final int iterations = 20 * 1000;
    private static final int saltLength = 32;
    private static final int desiredKeyLength = 256;


    /**
     * Hash a password for storage.
     * @param password - password.
     * @param salt - salt.
     * @return - hash.
     * @throws NoSuchAlgorithmException if occurs.
     * @throws InvalidKeySpecException if occurs.
     */
    private static String hash(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations, desiredKeyLength);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(secretKey.getEncoded());
    }

    /**
     * Computes a salted PBKDF2 hash of given plaintext password
     * suitable for storing in a database. Empty passwords are not supported.
     * @param password - password.
     * @return - salted hash.
     * @throws NoSuchAlgorithmException if occurs.
     * @throws InvalidKeySpecException if occurs.
     */
    public static String getSaltedHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        return encoder.encodeToString(salt) + hash(password, salt);
    }

    /**
     * Checks whether given plaintext password corresponds to a stored salted hash of the password.
     * @param password - given plaintext password.
     * @param storedPassword - stored salted hash of the password.
     * @return - boolean.
     * @throws NoSuchAlgorithmException if occurs.
     * @throws InvalidKeySpecException if occurs.
     */
    public static boolean check(String password, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String salt = storedPassword.substring(0, storedPassword.length() / 2);
        String pass = storedPassword.substring(storedPassword.length() / 2);
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String hashOfInput = hash(password, decoder.decode(salt));
        return hashOfInput.equals(pass);
    }

}
