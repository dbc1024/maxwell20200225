package com.mobao360.audit.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mobao360.audit.entity.AuditAgentBasicInfo;
import com.mobao360.audit.service.IAuditAgentBasicInfoService;
import com.mobao360.base.BaseController;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.EndeUtil;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import com.mobao360.system.utils.valid.ValidGroup1;
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
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-15
 */
@RestController
@RequestMapping("/auditAgentBasicInfo")
@Log4j2
@Api(tags = "审核-代理基本信息")
public class AuditAgentBasicInfoController extends BaseController {

    @Autowired
    private IAuditAgentBasicInfoService auditAgentBasicInfoService;



    @ApiOperation("变更新增事件")
    @PostMapping("/changeCreate")
    public Result changeCreate(@RequestBody @Validated AuditAgentBasicInfo auditAgentBasicInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditAgentBasicInfoService.changeCreate(auditAgentBasicInfo);

        return Result.success(auditAgentBasicInfo);
    }

    @ApiOperation("变更修改事件")
    @PostMapping("/changeUpdate")
    public Result changeUpdate(@RequestBody @Validated({ValidGroup1.class}) AuditAgentBasicInfo auditAgentBasicInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditAgentBasicInfoService.changeUpdate(auditAgentBasicInfo);

        return Result.success(auditAgentBasicInfo);
    }

    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditAgentBasicInfo auditAgentBasicInfo = auditAgentBasicInfoService.getById(id);
        Map<String, Object> keyValueHandle = DictionaryUtil.keyValueHandle(auditAgentBasicInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(keyValueHandle);
        return Result.success(keyValueHandle);
    }


    @ApiOperation("根据审核事件ID详情查询")
    @GetMapping("/detail/eventId/{eventId}")
    public Result getByEventId(@PathVariable Long eventId){

        LambdaQueryWrapper<AuditAgentBasicInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditAgentBasicInfo::getAuditEventId, eventId);

        AuditAgentBasicInfo agentBasicInfo = auditAgentBasicInfoService.getOne(wrapper);
        Map<String, Object> info = DictionaryUtil.keyValueHandle(agentBasicInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(info);
        return Result.success(info);
    }

    @ApiOperation(value = "草稿修改" , notes = "只能修改草稿信息")
    @PostMapping("/updateDraft")
    public Result updateDraft(@RequestBody @Validated({ValidGroup2.class}) AuditAgentBasicInfo auditAgentBasicInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditAgentBasicInfoService.updateDraft(auditAgentBasicInfo);

        return Result.success(auditAgentBasicInfo);
    }





    public void decrypt(Map<String, Object> agentBasicInfo) {
        if(agentBasicInfo == null){
            return;
        }

        Object legalPersonCertNum = agentBasicInfo.get("legalPersonCertNum");
        Object contactCertNum = agentBasicInfo.get("contactCertNum");

        //全部解密 用于修改信息
        agentBasicInfo.put("legalPersonCertNum", EndeUtil.decrypt(legalPersonCertNum));
        agentBasicInfo.put("contactCertNum", EndeUtil.decrypt(contactCertNum));

        //部分解密 用于查看
        agentBasicInfo.put("legalPersonCertNumEn", EndeUtil.decryptCert(legalPersonCertNum));
        agentBasicInfo.put("contactCertNumEn", EndeUtil.decryptCert(contactCertNum));
    }

}

