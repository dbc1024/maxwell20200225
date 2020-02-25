package com.mobao360.audit.controller;

import com.mobao360.audit.service.IAuditPersonalService;
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
 * @author: CSZ 991587100@qq.com
 * @date: 2019/4/9 09:56
 */
@RestController
@RequestMapping("/personal")
@Log4j2
@Api(tags = "审核-个人相关")
public class AuditPersonalController {

    @Autowired
    private IAuditPersonalService auditPersonalService;


    /**
     *个人锁定
     * @param params 入参：customerNo：客户号，description：申请意见
     */
    @ApiOperation(value = "锁定事件")
    @PostMapping("/lock")
    public Result lock(@RequestBody Map<String,String> params) {
        auditPersonalService.lock(params);
        return Result.success();
    }

    /**
     *个人解锁
     * @param params 入参：customerNo：客户号，description：申请意见
     */
    @ApiOperation(value = "解锁事件")
    @PostMapping("/unlock")
    public Result unlock(@RequestBody Map<String,String> params) {
        auditPersonalService.unlock(params);
        return Result.success();
    }

    /**
     *个人注销
     * @param params 入参：customerNo：客户号，description：申请意见
     */
    @ApiOperation(value = "注销事件")
    @PostMapping("/logout")
    public Result logout(@RequestBody Map<String,String> params) {
        auditPersonalService.logout(params);
        return Result.success();
    }



}
