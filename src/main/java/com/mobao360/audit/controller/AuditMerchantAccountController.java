package com.mobao360.audit.controller;

import com.mobao360.audit.service.IAuditMerchantAccountService;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Miskai
 * @date 2019/3/7
 */
@RestController
@RequestMapping("/merchantAccount")
@Log4j2
@Api(tags = "审核-商户账户")
public class AuditMerchantAccountController {

    @Autowired
    private IAuditMerchantAccountService iAuditMerchantAccountService;

    /**
     *商户账户锁定
     * @param params 入参：merchantAccount：账户号，description：申请意见
     */
    @ApiOperation(value = "锁定事件")
    @PostMapping("/lock")
    public Result lock(@RequestBody Map<String,String> params) {
        iAuditMerchantAccountService.lock(params);
        return Result.success();
    }

    /**
     *商户账户解锁
     * @param params 入参：merchantAccount：账户号，description：申请意见
     */
    @ApiOperation(value = "解锁事件")
    @PostMapping("/unlock")
    public Result unlock(@RequestBody Map<String,String> params) {
        iAuditMerchantAccountService.unlock(params);
        return Result.success();
    }

    /**
     *商户账户注销
     * @param params 入参：merchantAccount：账户号，description：申请意见
     */
    @ApiOperation(value = "注销事件")
    @PostMapping("/logout")
    public Result logout(@RequestBody Map<String,String> params) {
        iAuditMerchantAccountService.logout(params);
        return Result.success();
    }

}
