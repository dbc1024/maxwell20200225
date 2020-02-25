package com.mobao360.system.utils;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 新老客户号转换工具类
 * 由于新系统迁移了老系统的数据，部分需与外部系统交互的服务，在每次交互的时请调用本类trans()方法将商户号进行一次转换。
 * 交互的含义为：内部服务请求外部系统，或外部系统请求内部服务。
 * @author: CSZ 991587100@qq.com
 * @date: 2019/4/8 18:44
 */
public class CustNoTransUtil {

    private static Map<String, String> oldNewCustNo = new HashMap<>();
    private static Map<String, String> newOldCustNo = new HashMap<>();


    public static void main(String[] args) {
        System.out.println(CustNoTransUtil.newToOld("199900001017"));
        System.out.println(CustNoTransUtil.oldToNew("0842819484685683"));
        System.out.println(CustNoTransUtil.isOld("0842819484685683"));

        System.out.println(CustNoTransUtil.trans("199900001017"));
        System.out.println(CustNoTransUtil.trans("0842819484685683"));
        System.out.println(CustNoTransUtil.trans("199900001504"));
    }


    /**
     * 新客户号转换为老客户号
     * @param newCustNo
     * @return oldCustNo
     */
    public static String newToOld(String newCustNo){

        return newOldCustNo.get(newCustNo);
    }


    /**
     * 老客户号转换为新客户号
     * @param oldCustNo
     * @return newCustNo
     */
    public static String oldToNew(String oldCustNo){

        return oldNewCustNo.get(oldCustNo);
    }


    /**
     * 判断是否为老客户号
     * @param custNo
     * @return
     */
    public static boolean isOld(String custNo){
        if(StringUtils.isNotBlank(custNo) && custNo.length()==16){
            return true;
        }

        return false;
    }


    /**
     * 老客户号自动转换为新客户号
     * 新客户号自动转换为老客户号
     * 若为新系统的新商户则不做转换，返回原客户号
     * @param custNo
     * @return custNo
     */
    public static String trans(String custNo){

        String afterTrans = newOldCustNo.get(custNo);
        if(StringUtils.isBlank(afterTrans)){
            afterTrans = oldNewCustNo.get(custNo);
            if(StringUtils.isBlank(afterTrans)){
                return custNo;
            }
        }

        return afterTrans;
    }


    static {
        newOldCustNo.put("199900001017", "0842819484685683");
        newOldCustNo.put("199900001025", "0892404854743711");
        newOldCustNo.put("199900001033", "1194216166188548");
        newOldCustNo.put("199900001041", "1511396965332954");
        newOldCustNo.put("199900001058", "2992990445777167");
        newOldCustNo.put("199900001066", "1940098389615685");
        newOldCustNo.put("199900001074", "2284228147358414");
        newOldCustNo.put("199900001082", "2861572802777599");
        newOldCustNo.put("199900001090", "3167631406661732");
        newOldCustNo.put("199900001108", "3179537382075676");
        newOldCustNo.put("199900001116", "3403170081384072");
        newOldCustNo.put("199900001124", "3660861401018743");
        newOldCustNo.put("199900001132", "3784389191421038");
        newOldCustNo.put("199900001140", "3915133383940450");
        newOldCustNo.put("199900001157", "4037900708777938");
        newOldCustNo.put("199900001165", "4345975181909137");
        newOldCustNo.put("199900001173", "4552112845494710");
        newOldCustNo.put("199900001181", "4780071977519630");
        newOldCustNo.put("199900001199", "4800655181549165");
        newOldCustNo.put("199900001207", "4916120268952998");
        newOldCustNo.put("199900001215", "5817539740738056");
        newOldCustNo.put("199900001223", "6432354345051361");
        newOldCustNo.put("199900001231", "6599581552702454");
        newOldCustNo.put("199900001249", "7081981245821339");
        newOldCustNo.put("199900001256", "7241213088645576");
        newOldCustNo.put("199900001264", "7559431674653731");
        newOldCustNo.put("199900001272", "7738564318430776");
        newOldCustNo.put("199900001280", "8332412198576400");
        newOldCustNo.put("199900001298", "8580318671220097");
        newOldCustNo.put("199900001306", "8650469051018722");
        newOldCustNo.put("199900001314", "8789318931855660");
        newOldCustNo.put("199900001322", "8920293433026385");
        newOldCustNo.put("199900001330", "9019200307759036");
        newOldCustNo.put("199900001348", "9124807116156260");
        newOldCustNo.put("199900001355", "9136247968816542");
        newOldCustNo.put("199900001363", "9162327418139003");
        newOldCustNo.put("199900001371", "9465280510958265");
        newOldCustNo.put("199900001389", "9754307327398297");
        newOldCustNo.put("199900001397", "2038710001874252");

        newOldCustNo.put("199900001991", "9683499681250439");

        for (Map.Entry<String, String> entry : newOldCustNo.entrySet()) {
            oldNewCustNo.put(entry.getValue(), entry.getKey());
        }
    }

}
