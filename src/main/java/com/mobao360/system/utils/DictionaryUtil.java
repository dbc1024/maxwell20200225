package com.mobao360.system.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.base.mapper.DictionaryMapper;
import com.mobao360.system.annotation.DictionaryCode;
import com.mobao360.system.exception.MobaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 数据字典处理工具类
 * @author: CSZ 991587100@qq.com
 * @date: 2018/11/15 14:22
 */
@Component
public class DictionaryUtil {
    @Autowired
    private DictionaryMapper dictionaryMapper;

    private static DictionaryUtil util;

    @PostConstruct
    public void init(){
        util = this;
        util.dictionaryMapper = this.dictionaryMapper;
    }

    private static Map<String, Map<String, String>> code2KeyValueMap = new LinkedHashMap<>(16, 0.75f, true);
    /**
     * 若查询结果集中包含数据字典相关内容，需要进行数据字典键值对转换，可调用此方法。
     * 只需把查询结果集当做入参传入，即可自动完成数据字典键值对转换。
     *
     * 此工具类重载了四个keyValueHandle()方法，
     * 分别用于处理Map封装结果集的分页查询、详情查询
     * 以及相应实体对象封装结果集的分页查询、详情查询
     * @param page
     */
    public static void keyValueHandle(IPage page){

        if(page == null){
            return;
        }

        List records = page.getRecords();
        if(records == null || records.size() == 0){
            return;
        }

        List<Map<String, Object>> beanMapList = new LinkedList<>();

        for (Object obj: records) {
            beanMapList.add(keyValueHandle(obj));
        }

        page.setRecords(beanMapList);
    }


    public static Map<String, Object> keyValueHandle(Object obj){

        if(obj == null){
            return null;
        }

        Map<String, Object> beanMap = new HashMap<>(16);

        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);

                String name = field.getName();
                // 排除序列化属性
                if ("serialVersionUID".equals(name)) {
                    continue;
                }
                Object value = field.get(obj);

                beanMap.put(name, value);

                if(field.isAnnotationPresent(DictionaryCode.class) && value != null){
                    DictionaryCode dictionaryCode = field.getAnnotation(DictionaryCode.class);
                    String dicType = dictionaryCode.value();

                    Map<String, String> keyValueMap = getKeyValueMapByCode(dicType);
                    beanMap.put(name + "Dic", keyValueMap.get(value));
                }
            }
        } catch (Exception e) {
            throw new MobaoException("数据字典转换异常", e);
        }

        return beanMap;
    }


    /**
     * @param data
     * @return
     */
    public static void keyValueHandle(List<Map<String, String>> data){

        if (data==null || data.size()==0){
            return;
        }

        for (Map<String, String> map : data) {
            keyValueHandle(map);
        }

    }


    public static void keyValueHandle(Map<String, String> data){
        boolean isContainDic = false;

        if (data==null || data.size()==0){
            return;
        }

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (key.contains("-") && value!=null){
                isContainDic = true;

                Map<String, String> keyValueMap = getKeyValueMapByCode(key.split("-")[1]);
                entry.setValue(keyValueMap.get(value));
            }
        }

        if (!isContainDic){
            return;
        }

        //将不规则key值还原成原始key值
        Map<String, String> primaryKeyMap = new LinkedHashMap<>();

        Iterator<Map.Entry<String, String>> it = data.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            if(key.contains("-")){
                primaryKeyMap.put(key.split("-")[0], entry.getValue());
                it.remove();
            }
        }

        data.putAll(primaryKeyMap);
    }


    /**
     * 根据数据字典类型码获取对应的数据字典值集合
     * @param typeCode
     * @return
     */
    public static Map<String, String> getKeyValueMapByCode(String typeCode){
        //利用缓存数据，避免重复查库
        Map<String, String> keyValueMap = code2KeyValueMap.get(typeCode);

        if(keyValueMap == null){
            List<Map<String, String>> keyValueList = util.dictionaryMapper.getKeyValueListByCode(typeCode);
            if(keyValueList.size() > 0){
                keyValueMap = new HashMap<>(8);
                for (Map<String, String> dic : keyValueList) {
                    keyValueMap.put(dic.get("value"), dic.get("name"));
                }
                //新数据加入缓存
                code2KeyValueMap.put(typeCode, keyValueMap);
            }
        }

        return keyValueMap;
    }


    public static List<Map<String, String>> getKeyValueListByCode(String typeCode){

        Map<String, String> keyValueMap = getKeyValueMapByCode(typeCode);

        if(keyValueMap != null){

            List<Map<String, String>> KeyValueList = new ArrayList<>();
            for (Map.Entry<String, String> entry : keyValueMap.entrySet()) {
                Map<String, String> dic = new HashMap<>(2);
                dic.put("value", entry.getKey());
                dic.put("name", entry.getValue());

                KeyValueList.add(dic);
            }

            return KeyValueList;
        }

        return null;
    }


    /**
     * 添加数据字典到缓存中
     * @param typeCode
     * @return
     */
    public static void putKeyValueMap(String typeCode, Map<String, String> keyValueMap){
        code2KeyValueMap.put(typeCode, keyValueMap);
    }


    /**
     * 对应数据字典增、删、改的方法中应该调用此方法,
     * 传入对应数据字典类型 typeCode
     * 清除对应 typeCode 的缓存数据
     * @param typeCode
     */
    public static void cleanDictionaryCache(String typeCode){

        if("0".equals(typeCode)){
            code2KeyValueMap.clear();
        }else {
            code2KeyValueMap.remove(typeCode);
        }
    }

}
