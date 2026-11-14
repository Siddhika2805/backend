package com.interviewhub.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Utility class for secure password hashing and verification using PBKDF2 with HMAC-SHA256.
 * Demonstrates Java Cryptography Architecture (JCA) without storing plain-text passwords.
 */
public final class PasswordUtil {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
        // Prevent instantiation
    }

    /**
     * Generates a cryptographically strong random salt.
     *
     * @return Base64-encoded salt string
     */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hashes a password using PBKDF2 and the supplied salt.
     *
     * @param password Plain-text password
     * @param salt Base64-encoded salt
     * @return Base64-encoded hash
     */
    public static String hashPassword(String password, String salt) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        if (salt == null || salt.isEmpty()) {
            throw new IllegalArgumentException("Salt cannot be null or empty.");
        }
        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error occurred while hashing password: " + e.getMessage(), e);
        }
    }

    /**
     * Verifies if a plain password matches the stored hash and salt.
     *
     * @param providedPassword Plain-text password entered by user
     * @param storedHash Stored Base64 hash
     * @param storedSalt Stored Base64 salt
     * @return true if valid, false otherwise
     */
    public static boolean verifyPassword(String providedPassword, String storedHash, String storedSalt) {
        if (providedPassword == null || storedHash == null || storedSalt == null) {
            return false;
        }
        String calculatedHash = hashPassword(providedPassword, storedSalt);
        return calculatedHash.equals(storedHash);
    }
}
