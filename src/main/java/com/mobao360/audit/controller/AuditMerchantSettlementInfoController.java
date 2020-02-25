package com.mobao360.audit.controller;

import com.mobao360.audit.entity.AuditMerchantSettlementInfo;
import com.mobao360.audit.service.IAuditMerchantSettlementInfoService;
import com.mobao360.base.BaseController;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.EndeUtil;
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
 * @author CSZ 991587100@qq.com
 * @since 2018-12-28
 */
@RestController
@RequestMapping("/auditMerchantSettlementInfo")
@Log4j2
@Api(tags = "审核-商户结算信息")
public class AuditMerchantSettlementInfoController extends BaseController {

    @Autowired
    private IAuditMerchantSettlementInfoService auditMerchantSettlementInfoService;


    /**
     * 入网新增
     * 调此新增接口，实际是新增1条待审核的草稿结算信息
     * (无需生成审核事件，在商户基本资料入网接口已生成)
     * @param auditMerchantSettlementInfo
     * @param bindingResult
     * @return
     */
    @ApiOperation("入网新增事件")
    @PostMapping("/netCreate")
    public Result accessNetworkCreate(@RequestBody @Validated({ValidGroup2.class}) AuditMerchantSettlementInfo auditMerchantSettlementInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantSettlementInfoService.accessNetworkCreate(auditMerchantSettlementInfo);

        return Result.success(auditMerchantSettlementInfo);
    }


    /**
     * 调此修改接口，实际是新增一条待审核的草稿。
     * （需生成审核事件）
     * @param auditMerchantSettlementInfo
     * @param bindingResult
     * @return
     */
    @ApiOperation("变更修改事件")
    @PostMapping("/changeUpdate")
    public Result changeUpdate(@RequestBody @Validated AuditMerchantSettlementInfo auditMerchantSettlementInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantSettlementInfoService.changeUpdate(auditMerchantSettlementInfo);

        return Result.success(auditMerchantSettlementInfo);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditMerchantSettlementInfo auditMerchantSettlementInfo = auditMerchantSettlementInfoService.getById(id);
        Map<String, Object> settlementInfo = DictionaryUtil.keyValueHandle(auditMerchantSettlementInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(settlementInfo);
        return Result.success(settlementInfo);
    }


    @ApiOperation("根据审核事件ID详情查询")
    @GetMapping("/detail/eventId/{eventId}")
    public Result getByEventId(@PathVariable Long eventId){

        AuditMerchantSettlementInfo auditMerchantInfo = auditMerchantSettlementInfoService.getByEventId(eventId);
        Map<String, Object> info = DictionaryUtil.keyValueHandle(auditMerchantInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(info);
        return Result.success(info);
    }

    @ApiOperation("根据客户号查询详情")
    @GetMapping("/detail/customerNo/{customerNo}")
    public Result getByCustomerNo(@PathVariable String customerNo){

        AuditMerchantSettlementInfo auditMerchantInfo = auditMerchantSettlementInfoService.getByCustomerNo(customerNo);
        Map<String, Object> info = DictionaryUtil.keyValueHandle(auditMerchantInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(info);
        return Result.success(info);
    }


    @ApiOperation("草稿修改")
    @PostMapping("/updateDraft")
    public Result updateDraft(@RequestBody @Validated({ValidGroup2.class}) AuditMerchantSettlementInfo auditMerchantSettlementInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantSettlementInfoService.updateDraft(auditMerchantSettlementInfo);

        return Result.success(auditMerchantSettlementInfo);
    }


    @ApiOperation("商户中心新增合作商户结算信息")
    @PostMapping("/merchantCentreCreate")
    public Result merchantCentreCreate(@RequestBody AuditMerchantSettlementInfo auditMerchantSettlementInfo){

        auditMerchantSettlementInfoService.merchantCentreCreateOrUpdate(auditMerchantSettlementInfo);

        return Result.success(auditMerchantSettlementInfo);
    }


    @ApiOperation("商户中心修改合作商户结算信息")
    @PostMapping("/merchantCentreUpdate")
    public Result merchantCentreUpdate(@RequestBody AuditMerchantSettlementInfo auditMerchantSettlementInfo){

        auditMerchantSettlementInfoService.merchantCentreCreateOrUpdate(auditMerchantSettlementInfo);

        return Result.success(auditMerchantSettlementInfo);
    }


    /**
     * 解密敏感信息
     * @param settlementInfo
     */
    public void decrypt(Map<String, Object> settlementInfo) {

        if(settlementInfo == null){
            return;
        }

        Object bankAccountName = settlementInfo.get("bankAccountName");
        Object payeeIdNum = settlementInfo.get("payeeIdNum");
        Object accountNo = settlementInfo.get("accountNo");
        Object reserveMobileNo = settlementInfo.get("reserveMobileNo");

        //全部解密，用于修改页面展示
        settlementInfo.put("bankAccountName", EndeUtil.decrypt(bankAccountName));
        settlementInfo.put("payeeIdNum", EndeUtil.decrypt(payeeIdNum));
        settlementInfo.put("accountNo", EndeUtil.decrypt(accountNo));
        settlementInfo.put("reserveMobileNo", EndeUtil.decrypt(reserveMobileNo));
        //部分解密，用于详情页面展示
        settlementInfo.put("bankAccountNameEn", EndeUtil.decryptName(bankAccountName));
        settlementInfo.put("payeeIdNumEn", EndeUtil.decryptCert(payeeIdNum));
        settlementInfo.put("accountNoEn", EndeUtil.decryptBankCardNo(accountNo));
        settlementInfo.put("reserveMobileNoEn", EndeUtil.decryptTel(reserveMobileNo));
    }

}

