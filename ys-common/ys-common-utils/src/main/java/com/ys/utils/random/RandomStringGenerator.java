package com.ys.utils.random;

import java.security.SecureRandom;

/**
 * 随机字符串生成工具类
 * 生成指定长度（16位）包含大写字母、小写字母、数字和特殊符号的随机字符串
 */
public class RandomStringGenerator {

    // 定义字符池：大写字母、小写字母、数字、常用特殊符号
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String CHAR_POOL = UPPER_CASE + LOWER_CASE + DIGITS;

    // 安全随机数生成器（比Random更安全，适合密码类场景）
    private static final SecureRandom SECURE_RANDOM;

    static {
        try {
            // 初始化安全随机数生成器，使用SHA1PRNG算法
            SECURE_RANDOM = SecureRandom.getInstance("SHA1PRNG");
        } catch (Exception e) {
            throw new RuntimeException("初始化随机数生成器失败", e);
        }
    }

    /**
     * 生成16位包含字母、数字、特殊符号的随机字符串
     * @return 16位随机字符串
     */
    public static String generate16BitRandomString() {
        return generateRandomString(16);
    }

    /**
     * 生成指定长度的随机字符串
     * @param length 字符串长度（必须大于0）
     * @return 指定长度的随机字符串
     * @throws IllegalArgumentException 当长度小于等于0时抛出
     */
    public static String generateRandomString(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("字符串长度必须大于0");
        }

        StringBuilder sb = new StringBuilder(length);
        int poolSize = CHAR_POOL.length();

        for (int i = 0; i < length; i++) {
            // 从字符池中随机选择一个字符
            int randomIndex = SECURE_RANDOM.nextInt(poolSize);
            sb.append(CHAR_POOL.charAt(randomIndex));
        }

        return sb.toString();
    }
}
