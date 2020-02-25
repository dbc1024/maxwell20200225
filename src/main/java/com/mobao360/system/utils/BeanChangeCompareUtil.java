package com.mobao360.system.utils;

import com.mobao360.customer.entity.MerchantCentreAccount;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 获取实体修改内容工具类
 * @author: CSZ 991587100@qq.com
 * @date: 2018/12/11 11:27
 */
public class BeanChangeCompareUtil {

    public static <T> String compare(T oldBean, T newBean) {
        StringBuilder compareResult = new StringBuilder();

        try {
            // 通过反射获取类的类型及字段信息
            Class clazz = oldBean.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // 排除序列化属性
                if ("serialVersionUID".equals(field.getName())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                // 获取对应属性值
                Method getMethod = pd.getReadMethod();
                Object oldValue = getMethod.invoke(oldBean);
                Object newValue = getMethod.invoke(newBean);

                if(oldValue != null && newValue != null){
                    if (!oldValue.toString().equals(newValue.toString())) {
                        compareResult.append("[" + field.getName() + ":" + oldValue + "-->" + newValue + "],");
                    }
                }else if (oldValue == null && newValue == null) {
                    continue;
                }else if (oldValue != null && newValue == null){
                    compareResult.append("[" + field.getName() + ":" + oldValue + "-->" + newValue + "],");
                }else if (oldValue == null && newValue != null){
                    compareResult.append("[" + field.getName() + ":" + oldValue + "-->" + newValue + "],");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(compareResult.length() != 0){
            compareResult.deleteCharAt(compareResult.lastIndexOf(","));
        }

        return compareResult.toString();
    }


    public static void main(String[] args) {
        MerchantCentreAccount old = new MerchantCentreAccount();
        old.setManagerEmail("123@qq.com");
        old.setManagerTel("13458796958");

        MerchantCentreAccount now = new MerchantCentreAccount();
        now.setManagerEmail("234@qq.com");
        now.setManagerTel("13458796958");


        System.out.println(compare(old, now));

    }



}
