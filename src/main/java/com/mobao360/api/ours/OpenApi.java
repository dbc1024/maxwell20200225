package com.mobao360.api.ours;

import com.alibaba.fastjson.JSON;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.base.entity.BankCode;
import com.mobao360.channel.entity.ChannelFeeRate;
import com.mobao360.customer.entity.MerchantFeeRate;
import com.mobao360.system.dto.LastTradeOutDto;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/1/18 19:13
 */
@RestController
@RequestMapping("/api")
@Log4j2
@Api(tags = "公共-开放给其他微服务的接口")
public class OpenApi extends BaseOpenApi {

    private Logger logger= LoggerFactory.getLogger(OpenApi.class);


    @ApiOperation(value = "[001]根据客户号获取商户信息", notes = "传入客户号，如：199900000019")
    @GetMapping("/001")
    public Result queryMerchantInfoByCustomerNo(@RequestParam String customerNo){
        Map<String, Object> aboutMerchantInfo = super.getMerchantInfoByCustomerNo(customerNo);
        return Result.success(aboutMerchantInfo);
    }


    @ApiOperation(value = "[002]根据客户号获取开通的产品及支付方式", notes = "传入客户号，如：199900000019")
    @GetMapping("/002")
    public Result queryOpendProductsAndPayTypesByCustomerNo(@RequestParam String customerNo){
        Map<String, Object> productsAndPayTypes = super.getOpendProductsAndPayTypesByCustomerNo(customerNo);
        return Result.success(productsAndPayTypes);
    }


    @ApiOperation(value = "[003]订单系统获取商户相关信息")
    @GetMapping("/003")
    public Result orderQueryMerchantInfo(@RequestParam Map<String, String> params){
        logger.info("[003]订单系统获取商户相关信息,入参："+ JSON.toJSONString(params));
        Map<String, Object> aboutMerchant = super.orderQueryMerchantInfoBase(params);
        Result<Map<String, Object>> result = Result.success(aboutMerchant);
        logger.info("[003]订单系统获取商户相关信息,输出："+ JSON.toJSONString(result));
        return result;
    }


    @ApiOperation(value = "[004]获取支持的银行列表", notes = "若传入payType，则查询支持对应支付方式的银行")
    @GetMapping("/004")
    public Result supportBanks(@RequestParam(required = false) String payType){
        List<BankCode> bankList = super.supportBanksBase(payType);
        return Result.success(bankList);
    }


    @ApiOperation(value = "[005]根据客户号获取商户结算相关信息", notes = "传入客户号，如：199900000019")
    @GetMapping("/005")
    public Result settlementInfo(@RequestParam String customerNo){
        Map<String, Object> aboutSettlementInfo = super.settlementInfoBase(customerNo);
        return Result.success(aboutSettlementInfo);
    }


    @ApiOperation(value = "[006]根据客户号,支付方式获取可用费率")
    @GetMapping("/006")
    public Result getEffectiveFeeRate(@RequestParam(required = false) String customerNo, @RequestParam String payType){
        MerchantFeeRate feeRate = super.merchantFeeRateService.getByCustomerNoAndPayType(customerNo, payType);
        return Result.success(feeRate);
    }


    @ApiOperation(value = "[007]获取可用的通道费率")
    @PostMapping("/007")
    public Result getEffectiveChannelFeeRate(@RequestBody Map<String, String> params){

        logger.info("[007]获取可用的通道费率,入参："+ JSON.toJSONString(params));
        ChannelFeeRate channelFeeRate = super.channelFeeRateService.getEffectiveChannelFeeRate(params);
        logger.info("[007]获取可用的通道费率,输出："+ JSON.toJSONString(channelFeeRate));

        return Result.success(channelFeeRate);
    }


    @ApiOperation(value = "[008]获取银行列表信息")
    @PostMapping("/008")
    public Result getBankCodeList(){
        Map<String, String> bankCodeList = super.getBankCodeListBase();
        return Result.success(bankCodeList);
    }


    @ApiOperation(value = "[009]数据字典键值对查询", notes = "根据‘数据字典类型code’值(如：pay_type)，查询与其对应的所有数据字典键值对")
    @GetMapping("/009")
    public Result getKeyValueMapByCode(@RequestParam String typeCode) {
        Map<String, String> keyValueMap = DictionaryUtil.getKeyValueMapByCode(typeCode);
        return Result.success(keyValueMap);
    }


    @ApiOperation(value = "[010]根据客户号获取商户开通的业务", notes = "传入客户号，如：199900000019")
    @GetMapping("/010")
    public Result getBusiness(@RequestParam String customerNo){
        Map<String, String> businesses = super.getBusinessBase(customerNo);
        return Result.success(businesses);
    }


    @ApiOperation(value = "[011]根据客户号列表获取商户基本信息列表")
    @GetMapping("/011")
    public Result getByCustomerNoList(@RequestParam List<String> customerNoList){
        List<Map<String, Object>> merchantInfoList = super.getByCustomerNoListBase(customerNoList);
        return Result.success(merchantInfoList);
    }


    @ApiOperation(value = "[012]获取所有客户信息")
    @GetMapping("/012")
    public Result getAllMerchantInfo(){
        List<Map<String, String>> all = super.merchantInfoService.queryAll();
        return Result.success(all);
    }

    @ApiOperation(value = "[013]获取入网商户总数")
    @GetMapping("/013")
    public Result countOpenedAmount(){
        Integer amount = super.countOpenedAmountBase();
        Map<String, Integer> data = new HashMap<>(1);
        data.put("amount", amount);
        return Result.success(data);
    }

    @ApiOperation(value = "[014]根据平台客户号获取其所有合作商户客户号", notes = "传入客户号，如：199900000019")
    @GetMapping("/014")
    public Result getSubByCustomerNo(@RequestParam String customerNo){
        List<String> customerNos = super.merchantInfoService.getSubByCustomerNo(customerNo);
        return Result.success(customerNos);
    }





    @ApiOperation(value = "[100]新增审核事件")
    @PostMapping("/100")
    public Result createAuditEvent(@RequestBody @Validated AuditEvent auditEvent, BindingResult bindingResult){
        RegexUtil.beanValidate(bindingResult);
        logger.info("[100]新增审核事件,入参："+ JSON.toJSONString(auditEvent));
        super.auditEventService.create(auditEvent);
        return Result.success(auditEvent);
    }


    @ApiOperation(value = "[101]回滚新增事件")
    @GetMapping("/101")
    public Result createRollback(@RequestParam Long auditEventId){
        super.rollbackCreate(auditEventId);
        return Result.success();
    }


    @ApiOperation(value = "[102]审核")
    @PostMapping("/102")
    public Result audit(@RequestBody Map<String, String> params){
        logger.info("[102]审核,入参："+ JSON.toJSONString(params));
        Map<String, String> result = super.eventAudit(params);
        return Result.success(result);
    }


    @ApiOperation(value = "[103]审核回滚")
    @GetMapping("/103")
    public Result auditRollback(@RequestParam Long auditEventId){
        super.rollbackAudit(auditEventId);
        return Result.success();
    }


    @ApiOperation(value = "[104]更新商户最后交易日期，最后出款日期")
    @PostMapping("/104")
    public Result updateLastTradeDate(@RequestBody LastTradeOutDto params){
        logger.info("[104]订单系统更新商户最后交易日期,入参："+ JSON.toJSONString(params));
        super.updateLastTradeDateBase(params);
        return Result.success();
    }


    @ApiOperation(value = "[105]将商户置为黑名单商户")
    @PostMapping("/105")
    public Result setBlacklist(@RequestBody Map<String, List<String>> params){
        logger.info("[105]风控将商户置为黑名单商户,入参："+ JSON.toJSONString(params));
        super.setBlacklistBase(params);
        return Result.success();
    }

}
