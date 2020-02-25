package com.mobao360.audit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mobao360.audit.entity.AuditMerchantFundSettlement;
import com.mobao360.audit.service.IAuditMerchantFundSettlementService;
import com.mobao360.base.BaseController;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import com.mobao360.system.utils.valid.ValidGroup1;
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
 * @since 2019-01-17
 */
@RestController
@RequestMapping("/auditMerchantFundSettlement")
@Log4j2
@Api(tags = "审核-商户即时结算")
public class AuditMerchantFundSettlementController extends BaseController {

    @Autowired
    private IAuditMerchantFundSettlementService auditMerchantFundSettlementService;


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditMerchantFundSettlement auditMerchantFundSettlement = auditMerchantFundSettlementService.getById(id);

        return Result.success(DictionaryUtil.keyValueHandle(auditMerchantFundSettlement));
    }

    /**
     * 只有处于 "草稿"状态的审核事件才能修改
     * @param auditMerchantFundSettlement
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "草稿修改" ,notes = "只能修改草稿信息")
    @PostMapping("/updateDraft")
    public Result updateDraft(@RequestBody @Validated(ValidGroup1.class) AuditMerchantFundSettlement auditMerchantFundSettlement, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantFundSettlementService.updateDraft(auditMerchantFundSettlement);

        return Result.success(auditMerchantFundSettlement);
    }

    @ApiOperation("变更修改事件")
    @PostMapping("/changeUpdate")
    public Result changeUpdate(@RequestBody @Validated(ValidGroup2.class) AuditMerchantFundSettlement auditMerchantFundSettlement, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantFundSettlementService.changeUpdate(auditMerchantFundSettlement);

        return Result.success(auditMerchantFundSettlement);
    }



    @ApiOperation("变更新增事件")
    @PostMapping("/changeCreate")
    public Result changeCreate(@RequestBody @Validated AuditMerchantFundSettlement auditMerchantFundSettlement, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantFundSettlementService.changeCreate(auditMerchantFundSettlement);

        return Result.success(auditMerchantFundSettlement);
    }


    @ApiOperation("根据审核事件ID详情查询")
    @GetMapping("/detail/eventId/{auditEventId}")
    public Result getByEventId(@PathVariable Long auditEventId){

        AuditMerchantFundSettlement auditMerchantFundSettlement = auditMerchantFundSettlementService.getOne(new LambdaQueryWrapper<AuditMerchantFundSettlement>()
                .eq(AuditMerchantFundSettlement::getAuditEventId, auditEventId));

        return Result.success(DictionaryUtil.keyValueHandle(auditMerchantFundSettlement));
    }
}

