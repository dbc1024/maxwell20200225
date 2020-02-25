package com.mobao360.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.api.feign.TradeFeign;
import com.mobao360.base.entity.BankCode;
import com.mobao360.base.mapper.OtherMapper;
import com.mobao360.base.service.IBankCodeService;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.entity.MerchantSettlementInfo;
import com.mobao360.customer.mapper.MerchantInfoMapper;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.customer.service.IMerchantSettlementInfoService;
import com.mobao360.system.constant.CCertType;
import com.mobao360.system.constant.CMerchantStatus;
import com.mobao360.system.constant.Constants;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.*;
import com.mobao360.system.vo.CertDueExcel;
import com.mobao360.system.vo.NoTradeExcel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Service
public class MerchantInfoServiceImpl extends ServiceImpl<MerchantInfoMapper, MerchantInfo> implements IMerchantInfoService {

    private Logger logger = LoggerFactory.getLogger(MerchantInfoServiceImpl.class);

    @Autowired
    private MerchantInfoMapper merchantInfoMapper;

    @Autowired
    private IMerchantSettlementInfoService merchantSettlementInfoService;

    @Autowired
    private IBankCodeService bankCodeService;

    @Autowired
    private OtherMapper otherMapper;

    @Autowired
    private TradeFeign tradeFeign;


    @Override
    public List<Map<String, String>> queryAll() {
        return merchantInfoMapper.queryAll();
    }

    @Override
    public List<Map<String, String>> pageQuery(Map<String, String> params) {

        String noTradeDays = params.get("noTradeDays");
        if(StringUtils.isNotBlank(noTradeDays)){
            String today = DateUtil.today();
            String noTradeDate = DateUtils.offsetDay(today, -Integer.valueOf(noTradeDays));
            params.put("noTradeDate", noTradeDate);
        }

        String legalPersonCertNum = params.get("legalPersonCertNum");
        if(StringUtils.isNotBlank(legalPersonCertNum)){
            params.put("legalPersonCertNum", EndeUtil.encrypt(legalPersonCertNum));
        }

        //商户中心平台商户查询其合作商户使用
        String platformCustomerNo = NetUtil.getCustomerNoFromHeader();
        if(StringUtils.isNotBlank(platformCustomerNo)){
            params.put("platformCustomerNo", platformCustomerNo);
        }

        List<Map<String, String>> merchantInfoList = merchantInfoMapper.pageQuery(params);
        DictionaryUtil.keyValueHandle(merchantInfoList);

        return merchantInfoList;
    }


    @Override
    public List<Map<String, Object>> certDuePage(Map<String, String> params) {

            String dueStartDate = params.get("dueStartDate");
            String dueEndDate = params.get("dueEndDate");
            String certDueStatus = params.get("certDueStatus");

            if((StringUtils.isBlank(dueStartDate) && StringUtils.isNotBlank(dueEndDate)) ||
                    (StringUtils.isNotBlank(dueStartDate) && StringUtils.isBlank(dueEndDate))){
                throw new MobaoException("证件失效开始日期和结束日期必须同时传入");
            }

            String today = DateUtil.today();
            String dueDate60 = DateUtils.offsetDay(today, 60);
            //构造查询条件
            if(StringUtils.isBlank(dueStartDate) && StringUtils.isBlank(dueEndDate)){
                //已过期
                if(Constants.NO.equals(certDueStatus)){
                    params.put("dueEndDate", today);

                //即将过期
                }else if(Constants.YES.equals(certDueStatus)){
                    params.put("dueStartDate", today);
                    params.put("dueEndDate", dueDate60);

                //全部(已过期and即将过期)
                }else {
                    params.put("dueEndDate", dueDate60);
                }
            }

            //查询符合条件的结果
            List<Map<String, Object>> merchantOutList = merchantInfoMapper.certDuePage(params);
            List<Map<String, Object>> merchantList = new LinkedList<>();
            merchantList.addAll(merchantOutList);
            merchantOutList.clear();

            for (Map<String, Object> merchant : merchantList) {
                String legalPersonCertPeriodEnd = merchant.get("legalPersonCertPeriodEnd").toString();
                String businessLicencePeriodEnd = merchant.get("businessLicencePeriodEnd").toString();
                String icpPeriodEnd = merchant.get("icpPeriodEnd").toString();

                Map<String, Object> merchantOut = new HashMap<>(16);
                merchantOut.put("id", merchant.get("id"));
                merchantOut.put("customerName", merchant.get("customerName"));
                merchantOut.put("customerNo", merchant.get("customerNo"));
                merchantOut.put("salesmanName", merchant.get("salesmanName"));
                merchantOut.put("settStatus", merchant.get("settStatus"));
                merchantOut.put("tradeStatus", merchant.get("tradeStatus"));


                //过期证件列表
                List<Map<String, String>> dueCertList = new LinkedList<>();

                //没输入过期时间段
                if(StringUtils.isBlank(dueStartDate) && StringUtils.isBlank(dueEndDate)){
                    //已过期
                    if(Constants.NO.equals(certDueStatus)){

                        if(DateUtils.compare(legalPersonCertPeriodEnd, today) < 0){
                            Map<String, String> dueCert = new HashMap<>(16);

                            dueCert.put("certStatus", "已过期");
                            dueCert.put("certDueDate", legalPersonCertPeriodEnd);

                            String legalPersonCertType = merchant.get("legalPersonCertType").toString();
                            if(CCertType.ID.equals(legalPersonCertType)){
                                dueCert.put("certType", "法人身份证");
                            }else if(CCertType.PASSPORT.equals(legalPersonCertType)) {
                                dueCert.put("certType", "法人护照");
                            }

                            dueCertList.add(dueCert);
                        }

                        if(DateUtils.compare(businessLicencePeriodEnd, today) < 0){
                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "已过期");
                            dueCert.put("certDueDate", businessLicencePeriodEnd);
                            dueCert.put("certType", "营业执照");

                            dueCertList.add(dueCert);
                        }

                        if(DateUtils.compare(icpPeriodEnd, today) < 0){
                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "已过期");
                            dueCert.put("certDueDate", icpPeriodEnd);
                            dueCert.put("certType", "ICP备案");

                            dueCertList.add(dueCert);
                        }

                        //即将过期
                    }else if(Constants.YES.equals(certDueStatus)){

                        if(DateUtils.compare(legalPersonCertPeriodEnd, today) >= 0 &&
                                DateUtils.compare(legalPersonCertPeriodEnd, dueDate60) <= 0){

                            Map<String, String> dueCert = new HashMap<>(16);

                            dueCert.put("certStatus", "即将过期");
                            dueCert.put("certDueDate", legalPersonCertPeriodEnd);

                            String legalPersonCertType = merchant.get("legalPersonCertType").toString();
                            if(CCertType.ID.equals(legalPersonCertType)){
                                dueCert.put("certType", "法人身份证");
                            }else if(CCertType.PASSPORT.equals(legalPersonCertType)) {
                                dueCert.put("certType", "法人护照");
                            }

                            dueCertList.add(dueCert);
                        }

                        if(DateUtils.compare(businessLicencePeriodEnd, today) >= 0 &&
                                DateUtils.compare(businessLicencePeriodEnd, dueDate60) <= 0){

                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "即将过期");
                            dueCert.put("certDueDate", businessLicencePeriodEnd);
                            dueCert.put("certType", "营业执照");

                            dueCertList.add(dueCert);
                        }

                        if(DateUtils.compare(icpPeriodEnd, today) >= 0 &&
                                DateUtils.compare(icpPeriodEnd, dueDate60) <= 0){

                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "即将过期");
                            dueCert.put("certDueDate", icpPeriodEnd);
                            dueCert.put("certType", "ICP备案");

                            dueCertList.add(dueCert);
                        }

                        //全部（即将过期和已过期）
                    }else {

                        if(DateUtils.compare(legalPersonCertPeriodEnd, today) < 0){
                            Map<String, String> dueCert = new HashMap<>(16);

                            dueCert.put("certStatus", "已过期");
                            dueCert.put("certDueDate", legalPersonCertPeriodEnd);

                            String legalPersonCertType = merchant.get("legalPersonCertType").toString();
                            if(CCertType.ID.equals(legalPersonCertType)){
                                dueCert.put("certType", "法人身份证");
                            }else if(CCertType.PASSPORT.equals(legalPersonCertType)) {
                                dueCert.put("certType", "法人护照");
                            }

                            dueCertList.add(dueCert);
                        }else if(DateUtils.compare(legalPersonCertPeriodEnd, today) >= 0 &&
                                DateUtils.compare(legalPersonCertPeriodEnd, dueDate60) <= 0){

                            Map<String, String> dueCert = new HashMap<>(16);

                            dueCert.put("certStatus", "即将过期");
                            dueCert.put("certDueDate", legalPersonCertPeriodEnd);

                            String legalPersonCertType = merchant.get("legalPersonCertType").toString();
                            if(CCertType.ID.equals(legalPersonCertType)){
                                dueCert.put("certType", "法人身份证");
                            }else if(CCertType.PASSPORT.equals(legalPersonCertType)) {
                                dueCert.put("certType", "法人护照");
                            }

                            dueCertList.add(dueCert);
                        }

                        if(DateUtils.compare(businessLicencePeriodEnd, today) < 0){
                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "已过期");
                            dueCert.put("certDueDate", businessLicencePeriodEnd);
                            dueCert.put("certType", "营业执照");

                            dueCertList.add(dueCert);
                        }else if(DateUtils.compare(businessLicencePeriodEnd, today) >= 0 &&
                                DateUtils.compare(businessLicencePeriodEnd, dueDate60) <= 0){

                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "即将过期");
                            dueCert.put("certDueDate", businessLicencePeriodEnd);
                            dueCert.put("certType", "营业执照");

                            dueCertList.add(dueCert);
                        }

                        if(DateUtils.compare(icpPeriodEnd, today) < 0){
                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "已过期");
                            dueCert.put("certDueDate", icpPeriodEnd);
                            dueCert.put("certType", "ICP备案");

                            dueCertList.add(dueCert);
                        }else if(DateUtils.compare(icpPeriodEnd, today) >= 0 &&
                                DateUtils.compare(icpPeriodEnd, dueDate60) <= 0){

                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "即将过期");
                            dueCert.put("certDueDate", icpPeriodEnd);
                            dueCert.put("certType", "ICP备案");

                            dueCertList.add(dueCert);
                        }
                    }

                //输入了过期时间段
                }else {

                    if(DateUtils.compare(legalPersonCertPeriodEnd, dueStartDate) >= 0 &&
                            DateUtils.compare(legalPersonCertPeriodEnd, dueEndDate) <= 0){

                        if(DateUtils.compare(legalPersonCertPeriodEnd, today) < 0){
                            Map<String, String> dueCert = new HashMap<>(16);

                            dueCert.put("certStatus", "已过期");
                            dueCert.put("certDueDate", legalPersonCertPeriodEnd);

                            String legalPersonCertType = merchant.get("legalPersonCertType").toString();
                            if(CCertType.ID.equals(legalPersonCertType)){
                                dueCert.put("certType", "法人身份证");
                            }else if(CCertType.PASSPORT.equals(legalPersonCertType)) {
                                dueCert.put("certType", "法人护照");
                            }

                            dueCertList.add(dueCert);
                        }else if(DateUtils.compare(legalPersonCertPeriodEnd, today) >= 0 &&
                                DateUtils.compare(legalPersonCertPeriodEnd, dueDate60) <= 0){

                            Map<String, String> dueCert = new HashMap<>(16);

                            dueCert.put("certStatus", "即将过期");
                            dueCert.put("certDueDate", legalPersonCertPeriodEnd);

                            String legalPersonCertType = merchant.get("legalPersonCertType").toString();
                            if(CCertType.ID.equals(legalPersonCertType)){
                                dueCert.put("certType", "法人身份证");
                            }else if(CCertType.PASSPORT.equals(legalPersonCertType)) {
                                dueCert.put("certType", "法人护照");
                            }

                            dueCertList.add(dueCert);
                        }else {

                            Map<String, String> dueCert = new HashMap<>(16);

                            dueCert.put("certStatus", "正常");
                            dueCert.put("certDueDate", legalPersonCertPeriodEnd);

                            String legalPersonCertType = merchant.get("legalPersonCertType").toString();
                            if(CCertType.ID.equals(legalPersonCertType)){
                                dueCert.put("certType", "法人身份证");
                            }else if(CCertType.PASSPORT.equals(legalPersonCertType)) {
                                dueCert.put("certType", "法人护照");
                            }

                            dueCertList.add(dueCert);
                        }

                    }


                    if(DateUtils.compare(businessLicencePeriodEnd, dueStartDate) >= 0 &&
                            DateUtils.compare(businessLicencePeriodEnd, dueEndDate) <= 0){

                        if(DateUtils.compare(businessLicencePeriodEnd, today) < 0){
                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "已过期");
                            dueCert.put("certDueDate", businessLicencePeriodEnd);
                            dueCert.put("certType", "营业执照");

                            dueCertList.add(dueCert);
                        }else if(DateUtils.compare(businessLicencePeriodEnd, today) >= 0 &&
                                DateUtils.compare(businessLicencePeriodEnd, dueDate60) <= 0){

                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "即将过期");
                            dueCert.put("certDueDate", businessLicencePeriodEnd);
                            dueCert.put("certType", "营业执照");

                            dueCertList.add(dueCert);
                        }else {
                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "正常");
                            dueCert.put("certDueDate", businessLicencePeriodEnd);
                            dueCert.put("certType", "营业执照");

                            dueCertList.add(dueCert);
                        }

                    }


                    if(DateUtils.compare(icpPeriodEnd, dueStartDate) >= 0 &&
                            DateUtils.compare(icpPeriodEnd, dueEndDate) <= 0){

                        if(DateUtils.compare(icpPeriodEnd, today) < 0){
                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "已过期");
                            dueCert.put("certDueDate", icpPeriodEnd);
                            dueCert.put("certType", "ICP备案");

                            dueCertList.add(dueCert);
                        }else if(DateUtils.compare(icpPeriodEnd, today) >= 0 &&
                                DateUtils.compare(icpPeriodEnd, dueDate60) <= 0){

                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "即将过期");
                            dueCert.put("certDueDate", icpPeriodEnd);
                            dueCert.put("certType", "ICP备案");

                            dueCertList.add(dueCert);
                        }else {
                            Map<String, String> dueCert = new HashMap<>(16);
                            dueCert.put("certStatus", "正常");
                            dueCert.put("certDueDate", icpPeriodEnd);
                            dueCert.put("certType", "ICP备案");

                            dueCertList.add(dueCert);
                        }
                    }
                }

                merchantOut.put("dueCertList", dueCertList);
                merchantOutList.add(merchantOut);

            }

        return merchantOutList;
    }

    
    @Override
    public List<CertDueExcel> certDueExcelDown(Map<String, String> params) {
        List<Map<String, Object>> excelList = new ArrayList<>();

        List<Map<String, Object>> data = certDuePage(params);
        for (Map<String, Object> datum : data) {
            Object customerNo = datum.get("customerNo");
            Object customerName = datum.get("customerName");
            Object salesmanName = datum.get("salesmanName");
            Object tradeStatus = datum.get("tradeStatus");
            Object settStatus = datum.get("settStatus");

            List<Map<String, String>> dueCertList = (List<Map<String, String>>) datum.get("dueCertList");
            for (Map<String, String> dueCert : dueCertList) {
                String certType = dueCert.get("certType");
                String certStatus = dueCert.get("certStatus");
                String certDueDate = dueCert.get("certDueDate");

                Map<String, Object> dueCertTemp = new HashMap<>(1);
                dueCertTemp.put("customerNo", customerNo);
                dueCertTemp.put("customerName", customerName);
                dueCertTemp.put("salesmanName", salesmanName);
                dueCertTemp.put("tradeStatus", tradeStatus);
                dueCertTemp.put("settStatus", settStatus);
                dueCertTemp.put("certType", certType);
                dueCertTemp.put("certStatus", certStatus);
                dueCertTemp.put("certDueDate", certDueDate);

                excelList.add(dueCertTemp);
            }
        }


        List<CertDueExcel> excelBeanList = new ArrayList<>();
        for (Map<String, Object> map : excelList) {

            CertDueExcel excelBean = BeanUtil.mapToBean(map, CertDueExcel.class, true);
            excelBeanList.add(excelBean);
        }


        return excelBeanList;
    }

    @Override
    public List<NoTradeExcel> noTradeExcelDown(Map<String, String> params) {

        List<NoTradeExcel> excelList = new ArrayList<>();

        List<Map<String, String>> data = pageQuery(params);
        for (Map<String, String> datum : data) {
            String createTime = datum.get("createTime");
            String lastTradeDate = datum.get("lastTradeDate");

            NoTradeExcel noTradeExcel = new NoTradeExcel();
            noTradeExcel.setCustomerNo(datum.get("customerNo"));
            noTradeExcel.setCustomerName(datum.get("name"));
            noTradeExcel.setCreateTime(createTime);
            noTradeExcel.setMerchantType(datum.get("merchantTypeDic"));
            noTradeExcel.setSalesmanName(datum.get("salesmanName"));
            noTradeExcel.setStaff(datum.get("staff"));
            noTradeExcel.setStatus(datum.get("statusDic"));
            noTradeExcel.setTradeStatus(datum.get("tradeStatus"));
            noTradeExcel.setSettStatus(datum.get("settStatus"));
            noTradeExcel.setLastTradeDate(lastTradeDate);
            noTradeExcel.setNoTradeDays(getNoTradeDays(createTime, lastTradeDate));

            excelList.add(noTradeExcel);
        }

        return excelList;
    }

    private Long getNoTradeDays(String createTime, String lastTradeDate){
        Long noTradeDays;
        String today = DateUtil.today();

        if(StringUtils.isBlank(lastTradeDate)){
            DateTime begin = DateUtil.parseDate(createTime);
            DateTime end = DateUtil.parseDate(today);
            noTradeDays = DateUtil.between(begin, end, DateUnit.DAY);
        }else {
            DateTime begin = DateUtil.parseDate(lastTradeDate);
            DateTime end = DateUtil.parseDate(today);
            noTradeDays = DateUtil.between(begin, end, DateUnit.DAY);
        }

        return noTradeDays;
    }


    @Override
    public boolean save(MerchantInfo merchantInfo) {

        return super.save(merchantInfo);
    }

    @Override
    public MerchantInfo getByCustomerNo(String customerNo) {

        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantInfo::getCustomerNo, customerNo);
        String platformCustomerNo = NetUtil.getCustomerNoFromHeader();
        /**
         * 商户中心查平台商户菜单配置时，会调用此方法查询平台商户的基本信息,并且方法上的参数customerNo也是平台商户的客户号
         * 此时Header中的存放的也是平台商户的客户号
         *
         * 判断条件：参数客户号==Header中客户号，则不是查询平台商户下的合作商户
         */
        if(StringUtils.isNotBlank(platformCustomerNo) && !customerNo.equals(platformCustomerNo)){
            wrapper.eq(MerchantInfo::getPlatformCustomerNo, platformCustomerNo);
        }

        MerchantInfo info = getOne(wrapper);

        return info;
    }

    @Override
    public Map<String, String> detailByCustomerNo(String customerNo) {

        Map<String, String> detail = merchantInfoMapper.detail(customerNo);

        return detail;
    }

    @Override
    public MerchantInfo findTradePushedData() {

        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(MerchantInfo::getTask1, Constants.YES);
        wrapper.isNull(MerchantInfo::getOldCustomerNo);
        wrapper.last("AND ROWNUM = 1");

        return getOne(wrapper);
    }

    @Override
    public MerchantInfo findRiskPushedData() {

        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(MerchantInfo::getRiskTask, Constants.YES);
        wrapper.last("AND ROWNUM = 1");

        return getOne(wrapper);
    }

    @Override
    public Map<String,Integer> countOpenQuantity(String period) {

        Date date = new Date();
        String today = DateUtil.format(date, "yyyy-MM-dd") + " 00:00:00";
        String beginOfWeek = DateUtil.format(DateUtil.beginOfWeek(date), "yyyy-MM-dd") + " 00:00:00";
        String beginOfMonth = DateUtil.format(DateUtil.beginOfMonth(date), "yyyy-MM-dd") + " 00:00:00";

        Map<String,Integer> result = new HashMap<>(3);

        if(StringUtils.isBlank(period)){

            LambdaQueryWrapper<MerchantInfo> dayWrapper = new LambdaQueryWrapper<>();
            dayWrapper.ge(MerchantInfo::getCreateTime, today);
            int dayCount = count(dayWrapper);
            result.put("day", dayCount);

            LambdaQueryWrapper<MerchantInfo> weekWrapper = new LambdaQueryWrapper<>();
            weekWrapper.ge(MerchantInfo::getCreateTime, beginOfWeek);
            int weekCount = count(weekWrapper);
            result.put("week", weekCount);

            LambdaQueryWrapper<MerchantInfo> monthWrapper = new LambdaQueryWrapper<>();
            monthWrapper.ge(MerchantInfo::getCreateTime, beginOfMonth);
            int monthCount = count(monthWrapper);
            result.put("month", monthCount);

        }else {
            LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
            if("day".equals(period)){
                wrapper.ge(MerchantInfo::getCreateTime, today);
                int dayCount = count(wrapper);
                result.put("day", dayCount);

            }else if ("week".equals(period)){
                wrapper.ge(MerchantInfo::getCreateTime, beginOfWeek);
                int weekCount = count(wrapper);
                result.put("week", weekCount);

            }else if ("month".equals(period)){
                wrapper.ge(MerchantInfo::getCreateTime, beginOfMonth);
                int monthCount = count(wrapper);
                result.put("month", monthCount);
            }
        }

        return result;
    }

    /**
     * 向交易服务推送老系统移植的开户数据(以接口的形式来触发此推送程序)
     */
    @Override
    public void pushOldData(String ifAll) {

        if("all".equals(ifAll)){
            //将所有定时任务标识置为不成功
            LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.isNotNull(MerchantInfo::getOldCustomerNo);

            MerchantInfo info = new MerchantInfo();
            info.setTask1(Constants.NO);

            update(info, wrapper);
        }


        logger.info("[推送程序]向交易服务推送老系统数据开户数据开始执行");

        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.ne(MerchantInfo::getTask1, Constants.YES);
        wrapper.isNotNull(MerchantInfo::getOldCustomerNo);

        List<MerchantInfo> list = list(wrapper);
        if(list == null || list.size()==0){
            logger.info("未检测到可推送开户数据");
            return;
        }
        int total = list.size();
        logger.info("检测到{}条可推送开户数据", total);



        //记录推送失败客户号
        List<String> fail = new ArrayList<>(4);
        for (MerchantInfo merchantInfo : list) {

            MerchantSettlementInfo settlementInfo = merchantSettlementInfoService.getByCustomerNo(merchantInfo.getCustomerNo());
            String bankCode;
            String branchBankNo = settlementInfo.getBranchBankNo();
            //如果包含字母，表明是老系统迁移数据，没有选择具体的开户行（或为国外的开户行），此字段直接存入的银行编码
            if(RegexUtil.containLetter(branchBankNo)){
                bankCode = branchBankNo;
            }else {
                BankCode bankCodeModel = bankCodeService.getCodeByBranchBankNo(settlementInfo.getBranchBankNo());
                bankCode = bankCodeModel.getBankCode();
            }


            Map<String, Object> data = new HashMap(16);
            data.put("brchNo", merchantInfo.getBranchNo());
            data.put("custNo", merchantInfo.getCustomerNo());
            data.put("custName", merchantInfo.getName());
            data.put("industryCode", merchantInfo.getIndustryCode());
            data.put("custType", "2");
            data.put("channel", "01");
            data.put("pwd", "null");
            data.put("bankAcct", settlementInfo.getAccountNo());
            data.put("bankAcctName", settlementInfo.getBankAccountName());
            data.put("bankCode", bankCode);
            List<Map<String, String>> accountBalance = otherMapper.accountBalanceList(merchantInfo.getCustomerNo());
            data.put("openlist", accountBalance);


            logger.info("开始推送数据："+ JSON.toJSONString(data));

            Result result;
            try {
                result = tradeFeign.moveOldData(data);
            }catch (Exception e){
                throw new MobaoException("交易服务调用失败", e);
            }

            logger.info("交易系统返回："+ JSON.toJSONString(result));
            if(!Constants.YES.equals(result.getRetCode())){

                //如果返回状态码为"CTS6101",则表明该商户已开户成功
                if("CTS6101".equals(result.getRetCode())){
                    logger.info("交易系统反馈该商户已开户成功,直接处理为已成功状态");
                }else {
                    logger.error("为交易服务推送开户数据,交易服务返回异常："+ result.getRetMsg());
                    logger.error("此条数据推送失败");
                    fail.add(merchantInfo.getCustomerNo());
                    return;
                }
            }

            logger.info("商户{}推送成功", merchantInfo.getCustomerNo());

        }

        //定时任务标识修改为已成功
        if(fail.size()==0){
            LambdaQueryWrapper<MerchantInfo> updateWrapper = new LambdaQueryWrapper<>();
            updateWrapper.isNotNull(MerchantInfo::getOldCustomerNo);

            MerchantInfo info = new MerchantInfo();
            info.setTask1(Constants.YES);

            update(info, updateWrapper);
        }else {

            LambdaQueryWrapper<MerchantInfo> updateWrapper = new LambdaQueryWrapper<>();
            updateWrapper.isNotNull(MerchantInfo::getOldCustomerNo);
            updateWrapper.notIn(MerchantInfo::getCustomerNo, fail);

            MerchantInfo info = new MerchantInfo();
            info.setTask1(Constants.YES);

            update(info, updateWrapper);
        }

        logger.info("老数据推送程序执行完成");
        logger.info("检测到共{}条推送数据,失败{}条", total, fail.size());
        logger.info("失败数据："+ JSON.toJSONString(fail));
    }

    @Override
    public List<MerchantInfo> checkDueCert(String today) {

        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantInfo::getStatus, CMerchantStatus.ACTIVITY);
        wrapper.and(
                i->i.lt(MerchantInfo::getLegalPersonCertPeriodEnd, today)
                .or().lt(MerchantInfo::getBusinessLicencePeriodEnd, today)
                .or().lt(MerchantInfo::getIcpPeriodEnd, today)
        );

        List<MerchantInfo> list = super.list(wrapper);
        return list;
    }

    @Override
    public List<String> getSubByCustomerNo(String customerNo) {

        LambdaQueryWrapper<MerchantInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantInfo::getPlatformCustomerNo, customerNo);
        List<MerchantInfo> list = super.list(wrapper);

        List<String> customerNos = new ArrayList<>();
        for (MerchantInfo merchantInfo : list) {
            customerNos.add(merchantInfo.getCustomerNo());
        }

        return customerNos;
    }
}
