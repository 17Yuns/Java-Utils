package com.yiqiyuns.EncryptUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author 17Yuns
 * @version 1.0
 */
public class Base64Util {
    /**
     * base64编码
     *
     */
    public static String base64Encode(String txt) {
        return Base64.getEncoder().encodeToString(txt.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * base64解码
     *
     */
    public static String base64Decode(String txt) {
        return new String(Base64.getDecoder().decode(txt), StandardCharsets.UTF_8);
    }
}
