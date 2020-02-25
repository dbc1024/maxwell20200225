package com.mobao360.audit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.audit.entity.AuditEventType;
import com.mobao360.audit.service.IAuditEventTypeService;
import com.mobao360.base.BaseController;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-07
 */
@RestController
@RequestMapping("/auditEventType")
@Log4j2
@Api(tags = "审核-审核类型")
public class AuditEventTypeController extends BaseController {

    @Autowired
    private IAuditEventTypeService auditEventTypeService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = auditEventTypeService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }






    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){

        LambdaQueryWrapper<AuditEventType> wrapper = new LambdaQueryWrapper<>();

        String auditEventType = params.get("auditEventType");
        String auditModule = params.get("auditModule");
        if(StringUtils.isNotBlank(auditEventType)){
            wrapper.eq(AuditEventType::getAuditEventType, auditEventType);
        }
        if(StringUtils.isNotBlank(auditModule)){
            wrapper.eq(AuditEventType::getAuditModule, auditModule);
        }


        return wrapper;
    }
}

