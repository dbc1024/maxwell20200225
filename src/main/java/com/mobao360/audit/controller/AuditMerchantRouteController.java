package com.mobao360.audit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mobao360.audit.entity.AuditMerchantRoute;
import com.mobao360.audit.service.IAuditMerchantRouteService;
import com.mobao360.base.BaseController;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import com.mobao360.system.utils.valid.ValidGroup1;
import com.mobao360.system.utils.valid.ValidGroup2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author CSZ 991587100@qq.com
 * @since 2019-01-15
 */
@RestController
@RequestMapping("/auditMerchantRoute")
@Log4j2
@Api(tags = "审核-商户路由")
public class AuditMerchantRouteController extends BaseController {

    @Autowired
    private IAuditMerchantRouteService auditMerchantRouteService;



    /**
     * 路由变更新增事件（需生成审核事件）
     * @param route
     * @return
     */
    @ApiOperation("变更新增事件")
    @PostMapping("/changeCreate")
    public Result changeCreate(@RequestBody @Validated AuditMerchantRoute route, BindingResult bindingResult) {

        RegexUtil.beanValidate(bindingResult);
        auditMerchantRouteService.changeCreate(route);

        return Result.success(route);
    }


    /**
     * 路由变更修改事件（需生成审核事件）
     * @param route
     * @return
     */
    @ApiOperation("变更修改事件")
    @PostMapping("/changeUpdate")
    public Result changeUpdate(@RequestBody @Validated({ValidGroup2.class}) AuditMerchantRoute route, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantRouteService.changeUpdate(route);

        return Result.success(route);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditMerchantRoute auditMerchantRoute = auditMerchantRouteService.getById(id);

        return Result.success(DictionaryUtil.keyValueHandle(auditMerchantRoute));
    }


    @ApiOperation("草稿修改")
    @PostMapping("/updateDraft")
    public Result updateDraft(@RequestBody @Validated({ValidGroup1.class}) AuditMerchantRoute auditMerchantRoute, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantRouteService.updateDraft(auditMerchantRoute);

        return Result.success(auditMerchantRoute);
    }


    @ApiOperation("根据审核事件ID查询详情")
    @GetMapping("/detail/eventId/{auditEventId}")
    public Result getByEventId(@PathVariable Long auditEventId){

        AuditMerchantRoute auditMerchantRoute = auditMerchantRouteService.getByEventId(auditEventId);

        return Result.success(DictionaryUtil.keyValueHandle(auditMerchantRoute));
    }

}

