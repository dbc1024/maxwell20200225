package com.mobao360.system.utils;


import com.mobao360.system.exception.MobaoException;
import mobo.tool.SenseFieldTool;
import org.apache.commons.lang.StringUtils;

/**
 * 敏感信息字段加解密工具类
 * @author: CSZ 991587100@qq.com
 * @date: 2019/1/30 14:13
 */
public class EndeUtil {

    /**
     * 加密
     * @param plaintext
     * @return
     */
    public static String encrypt(String plaintext){
        if(plaintext == null || StringUtils.isBlank(plaintext)){
            return plaintext;
        }

        String ciphertext;

        try {
            ciphertext = SenseFieldTool.encryptMode(plaintext);
        } catch (Exception e) {
            throw new MobaoException("加密异常", e);
        }

        return ciphertext;
    }


    /**
     * 解密
     * @param ciphertext
     * @return
     */
    public static String decrypt(Object ciphertext){
        if(ciphertext == null || StringUtils.isBlank(ciphertext.toString())){
            return null;
        }

        String plaintext;
        try {
            plaintext = SenseFieldTool.decryptMode(ciphertext.toString());
        } catch (Exception e) {
            throw new MobaoException("解密异常", e);
        }

        return plaintext;
    }


    /**
     * 解密证件（身份证，护照）
     * 首1尾1，中间以*代替
     * @param ciphertext
     * @return
     */
    public static String decryptCert(Object ciphertext){

        String plaintext = decrypt(ciphertext);
        if(plaintext == null){
            return null;
        }
        int length = plaintext.length();

        String first = plaintext.substring(0,1);
        String last = plaintext.substring(length-1,length);
        String star = "";
        for (int i = 0; i < length-2; i++) {
            star = star + "*";
        }

        String partPlaintext = first + star + last;

        return partPlaintext;
    }


    /**
     * 解密手机号
     * 首3尾4，中间以*代替
     * @param ciphertext
     * @return
     */
    public static String decryptTel(Object ciphertext){

        String plaintext = decrypt(ciphertext);
        if(plaintext == null){
            return null;
        }
        int length = plaintext.length();

        String first = plaintext.substring(0,3);
        String last = plaintext.substring(length-4,length);
        String partPlaintext = first + "****" + last;

        return partPlaintext;
    }


    /**
     * 解密银行卡号
     * 首6尾4，中间以*代替
     * @param ciphertext
     * @return
     */
    public static String decryptBankCardNo(Object ciphertext){

        String plaintext = decrypt(ciphertext);
        if(plaintext == null){
            return null;
        }
        int length = plaintext.length();

        String first = plaintext.substring(0,6);
        String last = plaintext.substring(length-4,length);
        String star = "";
        for (int i = 0; i < length-10; i++) {
            star = star + "*";
        }

        String partPlaintext = first + star + last;

        return partPlaintext;
    }


    /**
     * 解密姓名
     * 首1尾1，中间以*代替
     * @param ciphertext
     * @return
     */
    public static String decryptName(Object ciphertext){

        String plaintext = decrypt(ciphertext);
        if(plaintext == null){
            return null;
        }
        int length = plaintext.length();

        String partPlaintext;
        String first = plaintext.substring(0,1);

        if(length <= 2){
            partPlaintext = first + "*";
        }else {
            String last = plaintext.substring(length-1,length);
            String star = "";
            for (int i = 0; i < length-2; i++) {
                star = star + "*";
            }

            partPlaintext = first + star + last;
        }

        return partPlaintext;
    }


}
