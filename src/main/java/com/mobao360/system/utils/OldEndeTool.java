package com.mobao360.system.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/6/3 08:55
 */
public class OldEndeTool {

    private static String key = "ABCDEFGHIJKLMNOPQRSTUVWX";

    public OldEndeTool() {
    }

    public static String encryptMode(String src) throws Exception {
        if(StringUtils.isBlank(src)){
            return src;
        }
        return Base64.encodeBase64String(encryptMode(key.getBytes("UTF-8"), src.getBytes("UTF-8")));
    }

    private static byte[] encryptMode(byte[] keybyte, byte[] src) throws Exception {
        SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
        Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        c1.init(1, deskey);
        byte[] emcrypt = c1.doFinal(src);
        return emcrypt;
    }

    public static String decryptMode(String src) throws Exception {
        if(StringUtils.isBlank(src)){
            return src;
        }
        return new String(decryptMode(key.getBytes(), Base64.decodeBase64(src)), "UTF-8");
    }

    private static byte[] decryptMode(byte[] keybyte, byte[] src) throws Exception {
        SecretKey deskey = new SecretKeySpec(keybyte, "DESede");
        Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        c1.init(2, deskey);
        return c1.doFinal(src);
    }

    public static void main(String[] args) throws Exception {
        String src = "zhb123456";
        String a = encryptMode(src);
        System.out.println(a);
        String b = decryptMode(a);
        System.out.println(b);
    }
}
