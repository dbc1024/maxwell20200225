package com.mobao360.audit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.base.BaseController;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-10
 */
@RestController
@RequestMapping("/auditEventDetail")
@Log4j2
@Api(tags = "审核-审核事件明细")
public class AuditEventDetailController extends BaseController {

    @Autowired
    private IAuditEventDetailService auditEventDetailService;



    @ApiOperation(value = "审核明细分页查询", notes = "入参：审核事件id[auditEventId]")
    @PostMapping("/auditDetailPage")
    public Result auditDetailPage(@RequestBody Map<String, String> params){

        IPage page = auditEventDetailService.auditDetailPage(prePageQueryPlus(params), params);
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation(value = "日志明细分页查询", notes = "入参：审核事件id[auditEventId],节点编码[nodeCode]")
    @PostMapping("/logDetailPage")
    public Result logDetailPage(@RequestBody Map<String, String> params){

        IPage page = auditEventDetailService.logDetailPage(prePageQueryPlus(params), params);
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditEventDetail auditEventDetail = auditEventDetailService.getById(id);
        return Result.success( DictionaryUtil.keyValueHandle(auditEventDetail));
    }

}

