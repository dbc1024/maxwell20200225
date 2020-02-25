package com.mobao360.audit.controller;

import com.mobao360.audit.entity.AuditMerchantFeeRate;
import com.mobao360.audit.service.IAuditMerchantFeeRateService;
import com.mobao360.base.BaseController;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import com.mobao360.system.utils.valid.ValidGroup1;
import com.mobao360.system.utils.valid.ValidGroup2;
import com.mobao360.system.utils.valid.ValidList;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author CSZ 991587100@qq.com
 * @since 2018-12-18
 */
@RestController
@RequestMapping("/auditMerchantFeeRate")
@Log4j2
@Api(tags = "审核-商户费率")
public class AuditMerchantFeeRateController extends BaseController {

    @Autowired
    private IAuditMerchantFeeRateService auditMerchantFeeRateService;


    /**
     * 调此新增接口，实际是新增n条待审核的草稿费率。
     * (无需生成审核事件，在商户基本资料入网接口已生成)
     * @param rateList
     * @return
     */
    @ApiOperation("入网新增事件")
    @PostMapping("/netCreate")
    public Result accessNetworkCreate(@RequestBody @Validated(ValidGroup1.class) ValidList<AuditMerchantFeeRate> rateList, BindingResult bindingResult) {

        RegexUtil.beanValidate(bindingResult);
        auditMerchantFeeRateService.accessNetworkCreate(rateList);

        return Result.success(rateList);
    }


    /**
     * 调此新增接口，实际是新增n条待审核的草稿费率。（需生成审核事件）
     * @param rateList
     * @return
     */
    @ApiOperation("变更新增事件")
    @PostMapping("/changeCreate")
    public Result changeCreate(@RequestBody @Validated ValidList<AuditMerchantFeeRate> rateList, BindingResult bindingResult) {

        RegexUtil.beanValidate(bindingResult);
        auditMerchantFeeRateService.changeCreate(rateList);

        return Result.success(rateList);
    }


    /**
     * 调此新增接口，实际是新增1条待审核的草稿费率。（需生成审核事件）
     * @param auditMerchantFeeRate
     * @return
     */
    @ApiOperation("变更修改事件")
    @PostMapping("/changeUpdate")
    public Result changeUpdate(@RequestBody @Validated(ValidGroup2.class) AuditMerchantFeeRate auditMerchantFeeRate, BindingResult bindingResult) {

        RegexUtil.beanValidate(bindingResult);
        auditMerchantFeeRateService.changeUpdate(auditMerchantFeeRate);

        return Result.success(auditMerchantFeeRate);
    }



    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditMerchantFeeRate auditMerchantFeeRate = auditMerchantFeeRateService.getById(id);

        return Result.success(DictionaryUtil.keyValueHandle(auditMerchantFeeRate));
    }


    @ApiOperation("根据审核事件ID查询")
    @GetMapping("/detail/eventId/{eventId}")
    public Result getByEventId(@PathVariable Long eventId){

        List<AuditMerchantFeeRate> rateList = auditMerchantFeeRateService.getByEventId(eventId);
        List<Map<String, Object>> rateMapList = new LinkedList<>();
        for (AuditMerchantFeeRate auditMerchantFeeRate : rateList) {
            Map<String, Object> stringObjectMap = DictionaryUtil.keyValueHandle(auditMerchantFeeRate);
            rateMapList.add(stringObjectMap);
        }

        return Result.success(rateMapList);
    }


    /**
     * 对于"商户入网"，"费率变更-新增"这两个事件
     * 对应的草稿修改操作，实际是批量新增，有以下几个步骤：
     *
     * 1.删除此事件对应的所有费率
     * 2.将所有数据的id置null
     * 3.批量新增费率草稿
     *
     * @param rateList
     * @return
     */
    @ApiOperation("批量草稿修改")
    @PostMapping("/batchUpdateDraft")
    public Result batchUpdateDraft(@RequestBody List<AuditMerchantFeeRate> rateList){

        auditMerchantFeeRateService.batchUpdateDraft(rateList);

        return Result.success(rateList);
    }


    /**
     * 对于"费率变更-修改"这个事件
     * 对应的草稿修改操作，就是单条修改
     *
     * @param auditMerchantFeeRate
     * @return
     */
    @ApiOperation("单条草稿修改")
    @PostMapping("/updateDraft")
    public Result updateDraft(@RequestBody AuditMerchantFeeRate auditMerchantFeeRate){

        auditMerchantFeeRateService.updateDraft(auditMerchantFeeRate);

        return Result.success(auditMerchantFeeRate);
    }

}

