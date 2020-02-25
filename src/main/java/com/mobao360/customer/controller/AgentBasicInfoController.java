package com.mobao360.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.base.BaseController;
import com.mobao360.customer.entity.AgentBasicInfo;
import com.mobao360.customer.service.IAgentBasicInfoService;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.EndeUtil;
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
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-15
 */
@RestController
@RequestMapping("/agentBasicInfo")
@Log4j2
@Api(tags = "客户-代理基本信息")
public class AgentBasicInfoController extends BaseController {

    @Autowired
    private IAgentBasicInfoService agentBasicInfoService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = agentBasicInfoService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AgentBasicInfo agentBasicInfo = agentBasicInfoService.getById(id);
        Map<String, Object> keyValueHandle = DictionaryUtil.keyValueHandle(agentBasicInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(keyValueHandle);

        return Result.success(keyValueHandle);
    }

    @ApiOperation("根据代理商号查询详情")
    @GetMapping("/detail/agentNo/{agentNo}")
    public Result detailByAgentNo(@PathVariable String agentNo){

        AgentBasicInfo agentBasicInfo = agentBasicInfoService.getByAgentNo(agentNo);
        Map<String, Object> keyValueHandle = DictionaryUtil.keyValueHandle(agentBasicInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(keyValueHandle);

        return Result.success(keyValueHandle);
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


    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<AgentBasicInfo> wrapper = new LambdaQueryWrapper<>();

        String agentNo = params.get("agentNo");
        String name = params.get("name");
        String contactPhone = params.get("contactPhone");
        String createStartTime = params.get("createStartTime");
        String createEndTime = params.get("createEndTime");

        if(StringUtils.isNotBlank(agentNo)){
            wrapper.eq(AgentBasicInfo::getAgentNo, agentNo);
        }
        if(StringUtils.isNotBlank(name)){
            wrapper.like(AgentBasicInfo::getName, name);
        }
        if(StringUtils.isNotBlank(contactPhone)){
            wrapper.eq(AgentBasicInfo::getContactPhone, contactPhone);
        }
        if(StringUtils.isNotBlank(createStartTime)){
            wrapper.ge(AgentBasicInfo::getCreateTime, createStartTime);
        }
        if(StringUtils.isNotBlank(createEndTime)){
            wrapper.le(AgentBasicInfo::getCreateTime, createEndTime);
        }

        return wrapper;
    }


}

