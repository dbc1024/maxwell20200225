package com.mobao360.audit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.base.BaseController;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import com.mobao360.system.utils.valid.ValidList;
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
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-10
 */
@RestController
@RequestMapping("/auditEventFlow")
@Log4j2
@Api(tags = "审核-审核流程")
public class AuditEventFlowController extends BaseController {

    @Autowired
    private IAuditEventFlowService auditEventFlowService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = auditEventFlowService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditEventFlow auditEventFlow = auditEventFlowService.getById(id);

        return Result.success(DictionaryUtil.keyValueHandle(auditEventFlow));
    }


    @ApiOperation("新增")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated ValidList<AuditEventFlow> flowList, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditEventFlowService.save(flowList);

        return Result.success(flowList);
    }


    @ApiOperation("修改")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated ValidList<AuditEventFlow> flowList, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditEventFlowService.update(flowList);

        return Result.success(flowList);
    }


    @ApiOperation("删除")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){

        auditEventFlowService.removeById(id);

        return Result.success();
    }




    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<AuditEventFlow> wrapper = new LambdaQueryWrapper<>();

        String auditEventType = params.get("auditEventType");

        if(StringUtils.isNotBlank(auditEventType)){
            wrapper.eq(AuditEventFlow::getAuditEventType, auditEventType);
        }

        return wrapper;
    }
}

