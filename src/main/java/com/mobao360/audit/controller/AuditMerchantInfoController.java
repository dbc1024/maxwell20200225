package com.mobao360.audit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.audit.entity.AuditMerchantInfo;
import com.mobao360.audit.service.IAuditMerchantInfoService;
import com.mobao360.base.BaseController;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.system.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-03
 */
@RestController
@RequestMapping("/auditMerchantInfo")
@Log4j2
@Api(tags = "审核-客户基本信息")
public class AuditMerchantInfoController extends BaseController {

    @Autowired
    private IAuditMerchantInfoService auditMerchantInfoService;
    @Autowired
    private IMerchantInfoService merchantInfoService;


    @ApiOperation("待审核合作商户分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = auditMerchantInfoService.page(prePageQueryPlus(params), condition(params));
        joinTable(page);
        DictionaryUtil.keyValueHandle(page);

        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    /**
     * 调此新增接口，实际是新增一条待审核的草稿。
     * （入网第一步由商户资料模块发起，所以此接口需生成审核事件）
     * @param auditMerchantInfo
     * @param bindingResult
     * @return
     */
    @ApiOperation("入网新增事件")
    @PostMapping("/netCreate")
    public Result accessNetworkCreate(@RequestBody @Validated AuditMerchantInfo auditMerchantInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantInfoService.accessNetworkCreate(auditMerchantInfo);

        return Result.success(auditMerchantInfo);
    }


    /**
     * 调此修改接口，实际是新增一条待审核的草稿。
     * （需生成审核事件）
     * @param auditMerchantInfo
     * @param bindingResult
     * @return
     */
    @ApiOperation("变更修改事件")
    @PostMapping("/changeUpdate")
    public Result changeUpdate(@RequestBody @Validated AuditMerchantInfo auditMerchantInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantInfoService.changeUpdate(auditMerchantInfo);

        return Result.success(auditMerchantInfo);
    }


    /**
     * 传入参数：客户号customerNo，明细描述description
     * @param params
     * @return
     */
    @ApiOperation("商户锁定事件")
    @PostMapping("/lock")
    public Result lock(@RequestBody Map<String, String> params){

        auditMerchantInfoService.lock(params);

        return Result.success();
    }


    /**
     * 传入参数：客户号customerNo，明细描述description
     * @param params
     * @return
     */
    @ApiOperation("商户解锁事件")
    @PostMapping("/unlock")
    public Result unlock(@RequestBody Map<String, String> params){

        auditMerchantInfoService.unlock(params);

        return Result.success();
    }

    /**
     * 传入参数：客户号customerNo，明细描述description
     * @param params
     * @return
     */
    @ApiOperation("商户注销事件")
    @PostMapping("/logout")
    public Result logout(@RequestBody Map<String, String> params){

        auditMerchantInfoService.logout(params);

        return Result.success();
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditMerchantInfo auditMerchantInfo = auditMerchantInfoService.getById(id);
        Map<String, Object> info = DictionaryUtil.keyValueHandle(auditMerchantInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(info);
        return Result.success(info);
    }

    @ApiOperation("根据审核事件ID查询")
    @GetMapping("/detail/eventId/{eventId}")
    public Result getByEventId(@PathVariable Long eventId){

        AuditMerchantInfo auditMerchantInfo = auditMerchantInfoService.getByEventId(eventId);
        Map<String, Object> info = DictionaryUtil.keyValueHandle(auditMerchantInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(info);
        return Result.success(info);
    }

    @ApiOperation("根据客户号查询详情")
    @GetMapping("/detail/customerNo/{customerNo}")
    public Result getByCustomerNo(@PathVariable String customerNo){

        AuditMerchantInfo auditMerchantInfo = auditMerchantInfoService.getByCustomerNo(customerNo);
        Map<String, Object> info = DictionaryUtil.keyValueHandle(auditMerchantInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(info);
        return Result.success(info);
    }


    @ApiOperation("草稿修改")
    @PostMapping("/updateDraft")
    public Result updateDraft(@RequestBody @Validated AuditMerchantInfo auditMerchantInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantInfoService.updateDraft(auditMerchantInfo);

        return Result.success(auditMerchantInfo);
    }


    /**
     * 商户中心新增平台商户下的合作商户
     * @param auditMerchantInfo
     * @param bindingResult
     * @return
     */
    @ApiOperation("商户中心新增合作商户")
    @PostMapping("/merchantCentreCreate")
    public Result merchantCentreCreate(@RequestBody @Validated AuditMerchantInfo auditMerchantInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantInfoService.merchantCentreCreate(auditMerchantInfo);

        return Result.success(auditMerchantInfo);
    }


    /**
     * 商户中心修改平台商户下的合作商户
     * @param auditMerchantInfo
     * @param bindingResult
     * @return
     */
    @ApiOperation("商户中心修改合作商户")
    @PostMapping("/merchantCentreUpdate")
    public Result merchantCentreUpdate(@RequestBody @Validated AuditMerchantInfo auditMerchantInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantInfoService.merchantCentreUpdate(auditMerchantInfo);

        return Result.success(auditMerchantInfo);
    }


    /**
     * 商户中心合作商户资料提交审核
     * 入参：{客户号_customerNo}
     * @param customerNo
     * @return
     */
    @ApiOperation(value = "商户中心合作商户资料提交审核", notes = "入参：{客户号_customerNo}")
    @GetMapping("/commitAudit/{customerNo}")
    public Result commitAudit(@PathVariable String customerNo){

        auditMerchantInfoService.updateStatusByCustomerNo(customerNo, "02");

        return Result.success();
    }







    private void decrypt(Map<String, Object> merchantInfo){

        if(merchantInfo == null){
            return;
        }

        //必填项
        Object legalPersonCertNum = merchantInfo.get("legalPersonCertNum");
        //非必填项
        Object contactCertNum = merchantInfo.get("contactCertNum");
        Object shareholderIdNum = merchantInfo.get("shareholderIdNum");
        Object staffCertNum = merchantInfo.get("staffCertNum");



        //全部解密，用于修改页面展示
        merchantInfo.put("legalPersonCertNum", EndeUtil.decrypt(legalPersonCertNum));
        merchantInfo.put("shareholderIdNum", EndeUtil.decrypt(shareholderIdNum));
        merchantInfo.put("contactCertNum", EndeUtil.decrypt(contactCertNum));
        merchantInfo.put("staffCertNum", EndeUtil.decrypt(staffCertNum));



        //部分解密，用于详情页面展示
        merchantInfo.put("legalPersonCertNumEn", EndeUtil.decryptCert(legalPersonCertNum));
        merchantInfo.put("shareholderIdNumEn", EndeUtil.decryptCert(shareholderIdNum));
        merchantInfo.put("contactCertNumEn", EndeUtil.decryptCert(contactCertNum));
        merchantInfo.put("staffCertNumEn", EndeUtil.decryptCert(staffCertNum));

    }


    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<AuditMerchantInfo> wrapper = new LambdaQueryWrapper<>();

        String platformCustomerNo = NetUtil.getCustomerNoFromHeader();
        if(StringUtils.isNotBlank(platformCustomerNo)){
            wrapper.eq(AuditMerchantInfo::getPlatformCustomerNo, platformCustomerNo);
            wrapper.ne(AuditMerchantInfo::getStatus, "99");
        }else {
            wrapper.isNotNull(AuditMerchantInfo::getPlatformCustomerNo);
            wrapper.and(i->i.eq(AuditMerchantInfo::getStatus, "02").or().eq(AuditMerchantInfo::getStatus, "98"));

            platformCustomerNo = params.get("platformCustomerNo");
            if(StringUtils.isNotBlank(platformCustomerNo)){
                wrapper.eq(AuditMerchantInfo::getPlatformCustomerNo, platformCustomerNo);
            }

        }

        String customerNo = params.get("customerNo");
        if(StringUtils.isNotBlank(customerNo)){
            wrapper.eq(AuditMerchantInfo::getCustomerNo, customerNo);
        }
        String name = params.get("name");
        if(StringUtils.isNotBlank(name)){
            wrapper.like(AuditMerchantInfo::getName, name);
        }
        String merchantType = params.get("merchantType");
        if(StringUtils.isNotBlank(merchantType)){
            wrapper.eq(AuditMerchantInfo::getMerchantType, merchantType);
        }
        String industryCode = params.get("industryCode");
        if(StringUtils.isNotBlank(industryCode)){
            wrapper.eq(AuditMerchantInfo::getIndustryCode, industryCode);
        }
        String status = params.get("status");
        if(StringUtils.isNotBlank(status)){
            wrapper.eq(AuditMerchantInfo::getStatus, status);
        }
        String commitTimeStart = params.get("commitTimeStart");
        if(StringUtils.isNotBlank(commitTimeStart)){
            wrapper.ge(AuditMerchantInfo::getUpdateTime, commitTimeStart);
        }
        String commitTimeEnd = params.get("commitTimeEnd");
        if(StringUtils.isNotBlank(commitTimeEnd)){
            wrapper.le(AuditMerchantInfo::getUpdateTime, commitTimeEnd);
        }

        return wrapper;
    }
    
    private void joinTable(IPage page){

        if(page == null){
            return;
        }

        List<AuditMerchantInfo> records = page.getRecords();
        if(records == null || records.size() == 0){
            return;
        }

        for (AuditMerchantInfo record : records) {
            String platformCustomerNo = record.getPlatformCustomerNo();
            if(StringUtils.isNotBlank(platformCustomerNo)){
                record.setPlatformCustomerName(merchantInfoService.getByCustomerNo(platformCustomerNo).getName());
            }
        }
    }

}

