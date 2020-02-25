package com.mobao360.base.controller;

import com.mobao360.base.entity.BankCode;
import com.mobao360.base.service.IBankCodeService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.mobao360.base.BaseController;


import java.util.Map;

/**
 * @author CSZ 991587100@qq.com
 * @since 2019-01-03
 */
@RestController
@RequestMapping("/bankCode")
@Log4j2
@Api(tags = "公共-银行编码")
public class BankCodeController extends BaseController {

    @Autowired
    private IBankCodeService bankCodeService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = bankCodeService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        BankCode bankCode = bankCodeService.getById(id);

        return Result.success( DictionaryUtil.keyValueHandle(bankCode));
    }


    @ApiOperation("新增")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated BankCode bankCode, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        bankCodeService.save(bankCode);

        return Result.success();
    }


    @ApiOperation("修改")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated BankCode bankCode, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        bankCodeService.updateById(bankCode);

        return Result.success();
    }


    @ApiOperation("删除")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){

        bankCodeService.removeById(id);

        return Result.success();
    }




    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<BankCode> wrapper = new LambdaQueryWrapper<>();

        String bankCode = params.get("bankCode");
        if(StringUtils.isNotBlank(bankCode)){
            wrapper.like(BankCode::getBankCode, bankCode);
        }
        String bankName = params.get("bankName");
        if (StringUtils.isNotBlank(bankName)) {
            wrapper.like(BankCode::getBankName, bankName);
        }
        String internetBankDebit = params.get("internetBankDebit");
        if (StringUtils.isNotBlank(internetBankDebit)) {
            wrapper.like(BankCode::getInternetBankDebit, internetBankDebit);
        }
        String internetBankCredit = params.get("internetBankCredit");
        if (StringUtils.isNotBlank(internetBankCredit)) {
            wrapper.like(BankCode::getInternetBankCredit, internetBankCredit);
        }
        String internetBankMix = params.get("internetBankMix");
        if (StringUtils.isNotBlank(internetBankMix)) {
            wrapper.like(BankCode::getInternetBankMix, internetBankMix);
        }
        String enterpriseInternetBank = params.get("enterpriseInternetBank");
        if (StringUtils.isNotBlank(enterpriseInternetBank)) {
            wrapper.like(BankCode::getEnterpriseInternetBank, enterpriseInternetBank);
        }
        String withdrawCash = params.get("withdrawCash");
        if (StringUtils.isNotBlank(withdrawCash)) {
            wrapper.like(BankCode::getWithdrawCash, withdrawCash);
        }
        String entrustPay = params.get("entrustPay");
        if (StringUtils.isNotBlank(entrustPay)) {
            wrapper.like(BankCode::getEntrustPay, entrustPay);
        }
        String agreementDebit = params.get("agreementDebit");
        if (StringUtils.isNotBlank(agreementDebit)) {
            wrapper.like(BankCode::getAgreementDebit, agreementDebit);
        }
        String agreementCredit = params.get("agreementCredit");
        if (StringUtils.isNotBlank(agreementCredit)) {
            wrapper.like(BankCode::getAgreementCredit, agreementCredit);
        }
        String quickDebit = params.get("quickDebit");
        if (StringUtils.isNotBlank(quickDebit)) {
            wrapper.like(BankCode::getQuickDebit, quickDebit);
        }
        String quickCredit = params.get("quickCredit");
        if (StringUtils.isNotBlank(quickCredit)) {
            wrapper.like(BankCode::getQuickCredit, quickCredit);
        }


        return wrapper;
    }
}

