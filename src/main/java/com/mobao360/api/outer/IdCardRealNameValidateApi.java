package com.mobao360.api.outer;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 第三方机构身份证实名认证接口
 * 机构：寻程数据
 * 官网：https://www.apistore.cn
 * @author: CSZ 991587100@qq.com
 * @date: 2018/11/29 09:09
 */
public class IdCardRealNameValidateApi {

    /** 请求地址：此接口校验内容为“身份证号码”与“姓名” */
    private static final String URL = "https://v.apistore.cn/api/a1";
    /** 申请的key */
    private static final String APPKEY = "6f3387aec6995f40364944951c009cb0";


    /** OCR请求地址：此接口识别内容为身份证正面照 */
    private static final String OCR_URL = "https://v.apistore.cn/api/c9";
    /** 申请的OCR key */
    private static final String OCR_APPKEY = "11d01ca2924169bf20d87286db36a115";


    /** 接口调用示例 */
    public static void main(String[] args) {

        Map<String, Object> params = new HashMap(8);
        params.put("cardNo","430512198908131367");
        params.put("realName","张牧之");

        JSONObject result = validate(params);
        System.out.println(result);
        //输出状态码，为0表示认证通过
        String code = result.getString("error_code");
        System.out.println(code);
        //认证结果原因
        String reason = result.getString("reason");
        System.out.println(reason);
    }

    /** OCR识别接口调用示例 */
    /*public static void main(String[] args) {

        String strBase64Image = getBase64Image("D:/sysini/IdCard.jpeg");

        Map<String, Object> params = new HashMap(8);
        params.put("bas64String", strBase64Image);

        JSONObject result = validateOCR(params);
        System.out.println(result);
        //输出状态码，为0表示识别成功
        String code = result.getString("error_code");
        System.out.println(code);
        //识别结果原因
        String reason = result.getString("reason");
        System.out.println(reason);
    }*/



    public static JSONObject validate(Map<String, Object> params){
        params.put("key",APPKEY);
        params.put("information","1");

        String resultStr = doPost(URL, urlencode(params));
        JSONObject result = JSONObject.parseObject(resultStr);

        return result;
    }

    public static JSONObject validateOCR(Map<String, Object> params){
        params.put("key",OCR_APPKEY);
        params.put("side","1");

        String resultStr = doPost(OCR_URL, urlencode(params));
        JSONObject result = JSONObject.parseObject(resultStr);

        return result;
    }

    /**
     * HTTP的Post请求方式
     * @param strUrl 访问地址
     * @param param 参数字符串
     */
    public static String doPost(String strUrl, String param) {
        String returnStr;
        URL url;
        HttpURLConnection httpURLConnection = null;

        try {
            url = new URL(strUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();

            byte[] byteParam = param.getBytes("UTF-8");
            DataOutputStream out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.write(byteParam);
            out.flush();
            out.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            returnStr = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return returnStr;
    }


    /**
     * 将map型转为请求参数型
     */
    public static String urlencode(Map<String,Object>data) {
        StringBuilder apistore = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                apistore.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return apistore.toString();
    }



    /**
     * 将图片转换为base64编码字符串
     */
    public static String getBase64Image(String imgPath) {

        InputStream inputStream;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgPath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Base64 base64 = new Base64();

        return base64.encodeAsString(data);
    }
}
