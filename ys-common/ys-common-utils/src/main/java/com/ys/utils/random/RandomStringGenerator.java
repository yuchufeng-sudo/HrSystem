package com.ys.utils.random;

import java.security.SecureRandom;

/**
 * Random String Generator Utility Class
 * Generates random strings of specified length containing uppercase letters, lowercase letters, and digits
 */
public class RandomStringGenerator {

    // Character pool definition: uppercase letters, lowercase letters, digits
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String CHAR_POOL = UPPER_CASE + LOWER_CASE + DIGITS;

    // Secure random number generator (more secure than Random, suitable for password scenarios)
    private static final SecureRandom SECURE_RANDOM;

    static {
        try {
            // Initialize secure random number generator using SHA1PRNG algorithm
            SECURE_RANDOM = SecureRandom.getInstance("SHA1PRNG");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize random number generator", e);
        }
    }

    /**
     * Generate a 16-character random string containing letters and digits
     * @return 16-character random string
     */
    public static String generate16BitRandomString() {
        return generateRandomString(16);
    }

    /**
     * Generate a random string of specified length
     * @param length String length (must be greater than 0)
     * @return Random string of specified length
     * @throws IllegalArgumentException if length is less than or equal to 0
     */
    public static String generateRandomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("String length must be greater than 0");
        }

        StringBuilder sb = new StringBuilder(length);
        int poolSize = CHAR_POOL.length();

        for (int i = 0; i < length; i++) {
            // Randomly select a character from the character pool
            int randomIndex = SECURE_RANDOM.nextInt(poolSize);
            sb.append(CHAR_POOL.charAt(randomIndex));
        }

        return sb.toString();
    }
}
