package com.yiqiyuns.EncryptUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;

/**
 * AES 加密工具类
 * @author woniu, 17Yuns
 * @version 2.0
 */
@SuppressWarnings("all")
public class AESUtils {
    /**
     * 加密模式之 ECB，算法/模式/补码方式
     */
    public static final String AES_ECB = "AES/ECB/PKCS5Padding";

    /**
     * 加密模式之 CBC，算法/模式/补码方式
     */
    public static final String AES_CBC = "AES/CBC/PKCS5Padding";

    /**
     * 加密模式之 CFB，算法/模式/补码方式
     */
    public static final String AES_CFB = "AES/CFB/PKCS5Padding";

    /**
     * AES 中的 IV 必须是 16 字节（128位）长
     */
    private static final Integer IV_LENGTH = 16;

    /***
     * 空校验
     * @param str 需要判断的值
     */
    public static boolean isEmpty(Object str) {
        return null == str || "".equals(str);
    }

    /***
     * String 转 byte
     * @param str 需要转换的字符串
     */
    public static byte[] getBytes(String str) {
        if (isEmpty(str)) {
            return null;
        }

        try {
            return str.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /***
     * 获取一个 AES 密钥规范
     */
    public static SecretKeySpec getSecretKeySpec(String key) {
        return new SecretKeySpec(Objects.requireNonNull(getBytes(key)), "AES");
    }


    /***
     * 初始化向量（IV），它是一个随机生成的字节数组，用于增加加密和解密的安全性
     */
    public static String getIV(){
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < IV_LENGTH ; i++){
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 加密 - 模式 ECB
     * @param text 需要加密的文本内容
     * @param key 加密的密钥 key
     * */
    public static String encrypt(String text, String key){
        if (isEmpty(text) || isEmpty(key)) {
            return null;
        }

        try {
            // 创建AES加密器
            Cipher cipher = Cipher.getInstance(AES_ECB);

            SecretKeySpec secretKeySpec = getSecretKeySpec(key);

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            // 加密字节数组
            byte[] encryptedBytes = cipher.doFinal(Objects.requireNonNull(getBytes(text)));

            // 将密文转换为 Base64 编码字符串
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密 - 模式 ECB
     * @param text 需要解密的文本内容
     * @param key 解密的密钥 key
     * */
    public static String decrypt(String text, String key){
        if (isEmpty(text) || isEmpty(key)) {
            return null;
        }

        // 将密文转换为16字节的字节数组
        byte[] textBytes = Base64.getDecoder().decode(text);

        try {
            // 创建AES加密器
            Cipher cipher = Cipher.getInstance(AES_ECB);

            SecretKeySpec secretKeySpec = getSecretKeySpec(key);

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            // 解密字节数组
            byte[] decryptedBytes = cipher.doFinal(textBytes);

            // 将明文转换为字符串
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加密 - 自定义加密模式
     *
     * @param text 需要加密的文本内容
     * @param key  加密的密钥 key
     * @param iv   初始化向量
     * @param mode 加密模式
     */
    public static String encrypt(String text, String key, String iv, String mode) {
        if (isEmpty(text) || isEmpty(key) || isEmpty(iv)) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance(mode);
            SecretKeySpec secretKeySpec = getSecretKeySpec(key);

            // 如果是 CBC 或 CFB 模式，则需要提供 IV 参数
            if (AES_CBC.equals(mode) || AES_CFB.equals(mode)) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(Objects.requireNonNull(getBytes(iv)));
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            } else {
                // 如果是其他模式（如 ECB），则直接进行加密
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            }

            // 加密字节数组
            byte[] encryptedBytes = cipher.doFinal(Objects.requireNonNull(getBytes(text)));

            // 将密文转换为 Base64 编码字符串
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解密 - 自定义加密模式
     *
     * @param text 需要解密的文本内容
     * @param key  解密的密钥 key
     * @param iv   初始化向量
     * @param mode 加密模式
     */
    public static String decrypt(String text, String key, String iv, String mode) {
        if (isEmpty(text) || isEmpty(key) || isEmpty(iv)) {
            return null;
        }

        // 将密文转换为16字节的字节数组
        byte[] textBytes = Base64.getDecoder().decode(text);

        try {
            Cipher cipher = Cipher.getInstance(mode);
            SecretKeySpec secretKeySpec = getSecretKeySpec(key);

            // 如果是 CBC 或 CFB 模式，则需要提供 IV 参数
            if (AES_CBC.equals(mode) || AES_CFB.equals(mode)) {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(Objects.requireNonNull(getBytes(iv)));
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            } else {
                // 如果是其他模式（如 ECB），则直接进行解密
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            }

            // 解密字节数组
            byte[] decryptedBytes = cipher.doFinal(textBytes);

            // 将明文转换为字符串
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
