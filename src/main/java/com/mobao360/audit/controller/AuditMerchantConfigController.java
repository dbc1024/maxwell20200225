package com.mobao360.audit.controller;

import com.mobao360.audit.entity.AuditMerchantConfig;
import com.mobao360.audit.service.IAuditMerchantConfigService;
import com.mobao360.base.BaseController;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import com.mobao360.system.utils.valid.ValidGroup2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-11
 */
@RestController
@RequestMapping("/auditMerchantConfig")
@Log4j2
@Api(tags = "审核-商户配置")
public class AuditMerchantConfigController extends BaseController {

    @Autowired
    private IAuditMerchantConfigService auditMerchantConfigService;


    @ApiOperation("入网新增事件")
    @PostMapping("/netCreate")
    public Result accessNetworkCreate(@RequestBody @Validated({ValidGroup2.class}) AuditMerchantConfig auditMerchantConfig, BindingResult bindingResult){
        RegexUtil.beanValidate(bindingResult);
        auditMerchantConfigService.accessNetworkCreate(auditMerchantConfig);
        return Result.success(auditMerchantConfig);
    }


    @ApiOperation("变更修改事件")
    @PostMapping("/changeUpdate")
    public Result changeUpdate(@RequestBody@Validated AuditMerchantConfig auditMerchantConfig, BindingResult bindingResult){
        RegexUtil.beanValidate(bindingResult);
        auditMerchantConfigService.changeUpdate(auditMerchantConfig);
        return Result.success(auditMerchantConfig);
    }


    /**
     * 交易关闭
     * 传入参数:客户号customerNo，明细描述description
     * @param params
     * @return
     */
    @ApiOperation("交易关闭事件")
    @PostMapping("/transactionClose")
    public Result transactionClose(@RequestBody Map<String,String> params){
        auditMerchantConfigService.transactionClose(params);
        return Result.success();
    }


    /**
     * 结算关闭
     * 传入参数:客户号customerNo，明细描述description
     * @param params
     * @return
     */
    @ApiOperation("结算关闭事件")
    @PostMapping("/settlementClose")
    public Result settlementClose(@RequestBody Map<String,String> params){
        auditMerchantConfigService.settlementClose(params);
        return Result.success();
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditMerchantConfig auditMerchantConfig = auditMerchantConfigService.getById(id);

        return Result.success(DictionaryUtil.keyValueHandle(auditMerchantConfig));
    }


    @ApiOperation("根据审核事件ID查询")
    @GetMapping("/detail/eventId/{eventId}")
    public Result getByEventId(@PathVariable Long eventId){

        AuditMerchantConfig auditMerchantConfig = auditMerchantConfigService.getByEventId(eventId);

        return Result.success(DictionaryUtil.keyValueHandle(auditMerchantConfig));
    }


    @ApiOperation(value = "草稿修改")
    @PostMapping("/updateDraft")
    public Result updateDraft(@RequestBody @Validated({ValidGroup2.class}) AuditMerchantConfig auditMerchantConfig, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantConfigService.updateDraft(auditMerchantConfig);

        return Result.success(auditMerchantConfig);
    }

}

