package com.mobao360.audit.controller;

import com.mobao360.audit.entity.AuditMerchantCentreAccount;
import com.mobao360.audit.service.IAuditMerchantCentreAccountService;
import com.mobao360.base.BaseController;
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
 * @since 2018-12-28
 */
@RestController
@RequestMapping("/auditMerchantCentreAccount")
@Log4j2
@Api(tags = "审核-商户中心账号")
public class AuditMerchantCentreAccountController extends BaseController {

    @Autowired
    private IAuditMerchantCentreAccountService merchantCentreAccountService;


    @ApiOperation("入网新增事件")
    @PostMapping("/netCreate")
    public Result accessNetworkCreate(@RequestBody @Validated({ValidGroup2.class}) AuditMerchantCentreAccount auditMerchantCentreAccount){

        merchantCentreAccountService.accessNetworkCreate(auditMerchantCentreAccount);

        return Result.success(auditMerchantCentreAccount);
    }


    @ApiOperation(value = "变更修改事件", notes = "需要提供商户号")
    @PostMapping("/changeUpdate")
    public Result changeUpdate(@RequestBody @Validated AuditMerchantCentreAccount auditMerchantCentreAccount){

        merchantCentreAccountService.changeUpdate(auditMerchantCentreAccount);

        return Result.success(auditMerchantCentreAccount);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditMerchantCentreAccount merchantCentreAccount = merchantCentreAccountService.getById(id);

        return Result.success(merchantCentreAccount);
    }


    @ApiOperation("根据审核事件ID查询")
    @GetMapping("/detail/eventId/{eventId}")
    public Result getByEventId(@PathVariable Long eventId){

        AuditMerchantCentreAccount merchantCentreAccount = merchantCentreAccountService.getByEventId(eventId);

        return Result.success(merchantCentreAccount);
    }


    @ApiOperation("草稿修改")
    @PostMapping("/updateDraft")
    public Result updateDraft(@RequestBody @Validated({ValidGroup2.class}) AuditMerchantCentreAccount merchantCentreAccount, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        merchantCentreAccountService.updateDraft(merchantCentreAccount);

        return Result.success(merchantCentreAccount);
    }

}

