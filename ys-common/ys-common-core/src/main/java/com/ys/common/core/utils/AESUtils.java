package com.ys.common.core.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES Encryption
 */
public class AESUtils {

    // Algorithm/Mode/Padding
    private static final String AES_CBC_PKCS5 = "AES/CBC/PKCS5Padding";

    private static final String AES = "AES";

    private static final String KEY = "hrSystemKey@2025";

    private static final String IV = "abcde123456@2025";
    /**
     * AES Encryption
     * @param plainText Plaintext
     */
    public static String encrypt(String plainText) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), AES);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * AES Decrypt
     */
    public static String decrypt(String cipherTextBase64) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), AES);
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5);
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherTextBase64));
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
