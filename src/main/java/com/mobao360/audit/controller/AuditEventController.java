package com.mobao360.audit.controller;

import com.github.pagehelper.Page;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.base.BaseController;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-10
 */
@RestController
@RequestMapping("/auditEvent")
@Log4j2
@Api(tags = "审核-审核事件")
public class AuditEventController extends BaseController {

    @Autowired
    private IAuditEventService auditEventService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        Page<Object> page = prePageQuery(params);
        auditEventService.pageQuery(params);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    /**
     *  审核接口
     *  入参：{审核事件ID}，{审核操作}（提交审核，通过，退回，拒绝，作废），{明细描述}。
     * @param params
     * @return
     */
    @ApiOperation(value = "审核", notes = "所有审核操作（提交审核，通过，退回，拒绝，作废）都调用此接口")
    @PostMapping("/audit")
    public Result audit(@RequestBody Map<String, String> params){

        auditEventService.auditMax(params);

        return Result.success();
    }


    /**
     * 删除审核事件及草稿,只有在事件处于"草稿"状态才能删除
     * @param eventId
     * @return
     */
    @ApiOperation("删除审核事件")
    @GetMapping("/delete/{eventId}")
    public Result deleteDraft(@PathVariable Long eventId){

        auditEventService.removeByEventId(eventId);
        return Result.success();
    }


    @ApiOperation("统计当前登录人待处理的事件数量")
    @GetMapping("/count/ongoing")
    public Result countDoing(){
        int num = auditEventService.countDoing();
        return Result.success(num);
    }


    /**
     *  商户中心合作商户资料审核
     *  入参：{客户号_customerNo}，{审核操作_operation}（通过-99，拒绝-98），{审核意见_auditAdvice}。
     * @param params
     * @return
     */
    @ApiOperation(value = "商户中心合作商户资料审核", notes = "入参：{客户号_customerNo}，{审核操作_operation}（通过-99，拒绝-98），{审核意见_auditAdvice}")
    @PostMapping("/merchantCentreAudit")
    public Result merchantCentreAudit(@RequestBody Map<String, String> params){

        auditEventService.merchantCentreAudit(params);

        return Result.success();
    }


}

