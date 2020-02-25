package com.mobao360.audit.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.mobao360.audit.entity.AuditMerchantElectronicData;
import com.mobao360.audit.service.IAuditMerchantElectronicDataService;
import com.mobao360.audit.service.IAuditMerchantInfoService;
import com.mobao360.base.BaseController;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import com.mobao360.system.utils.valid.ValidGroup2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-03
 */
@RestController
@RequestMapping("/auditMerchantElectronicData")
@Log4j2
@Api(tags = "审核-商户电子资料")
public class AuditMerchantElectronicDataController extends BaseController {

    @Autowired
    private IAuditMerchantElectronicDataService auditMerchantElectronicDataService;
    @Autowired
    private IAuditMerchantInfoService auditMerchantInfoService;


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        AuditMerchantElectronicData auditMerchantElectronicData = auditMerchantElectronicDataService.getById(id);

        Map<String, Object> electronicData = stringFieldToJsonObject(auditMerchantElectronicData);

        return Result.success(electronicData);
    }


    @ApiOperation("根据审核事件ID查询")
    @GetMapping("/detail/eventId/{eventId}")
    public Result getByEventId(@PathVariable Long eventId){

        AuditMerchantElectronicData auditMerchantElectronicData = auditMerchantElectronicDataService.getByEventId(eventId);

        Map<String, Object> electronicData = stringFieldToJsonObject(auditMerchantElectronicData);

        return Result.success(electronicData);
    }

    @ApiOperation("根据客户号查询详情")
    @GetMapping("/detail/customerNo/{customerNo}")
    public Result getByCustomerNo(@PathVariable String customerNo){

        AuditMerchantElectronicData auditMerchantElectronicData = auditMerchantElectronicDataService.getByCustomerNo(customerNo);

        Map<String, Object> electronicData = stringFieldToJsonObject(auditMerchantElectronicData);

        return Result.success(electronicData);
    }


    @ApiOperation("入网新增事件")
    @PostMapping("/netCreate")
    public Result accessNetworkCreate(@RequestBody @Validated({ValidGroup2.class}) AuditMerchantElectronicData merchantElectronicData) {

        auditMerchantElectronicDataService.accessNetworkCreate(merchantElectronicData);
        return Result.success(merchantElectronicData);
    }


    @ApiOperation("变更修改事件")
    @PostMapping("/changeUpdate")
    public Result changeUpdate(@RequestBody @Validated AuditMerchantElectronicData merchantElectronicData) {

        auditMerchantElectronicDataService.changeUpdate(merchantElectronicData);
        return Result.success(merchantElectronicData);
    }


    /**
     * 修改时需要添加一条日志明细记录
     * @param auditMerchantElectronicData
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "草稿修改" , notes = "只能修改处于草稿状态的记录")
    @PostMapping("/updateDraft")
    public Result updateDraft(@RequestBody @Validated({ValidGroup2.class}) AuditMerchantElectronicData auditMerchantElectronicData, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        auditMerchantElectronicDataService.updateDraft(auditMerchantElectronicData);

        return Result.success(auditMerchantElectronicData);
    }

    /**
     * 补充电子资料
     * @param auditMerchantElectronicData
     * @return
     */
    @ApiOperation(value = "补充电子资料")
    @PostMapping("/supplement")
    public Result supplement(@RequestBody @Validated({ValidGroup2.class}) AuditMerchantElectronicData auditMerchantElectronicData, BindingResult bindingResult){

        auditMerchantElectronicDataService.supplement(auditMerchantElectronicData);

        return Result.success();
    }


    @ApiOperation("商户中心新增合作商户电子资料")
    @PostMapping("/merchantCentreCreate")
    public Result merchantCentreCreate(@RequestBody AuditMerchantElectronicData merchantElectronicData) {
        String currTime = DateUtil.now();
        merchantElectronicData.setCreateTime(currTime);
        merchantElectronicData.setUpdateTime(currTime);
        auditMerchantElectronicDataService.save(merchantElectronicData);

        return Result.success(merchantElectronicData);
    }


    @ApiOperation("商户中心修改合作商户电子资料")
    @PostMapping("/merchantCentreUpdate")
    public Result merchantCentreUpdate(@RequestBody AuditMerchantElectronicData merchantElectronicData) {
        String currTime = DateUtil.now();
        merchantElectronicData.setUpdateTime(currTime);

        auditMerchantElectronicDataService.updateById(merchantElectronicData);
        // 拒绝再修改时，整体变为草稿状态
        auditMerchantInfoService.updateStatusByCustomerNo(merchantElectronicData.getCustomerNo(), "01");
        return Result.success(merchantElectronicData);
    }


    private Map<String, Object> stringFieldToJsonObject(AuditMerchantElectronicData electronicData){

        if(electronicData == null){
            return null;
        }

        //排除不需要转换的字段
        String exclude = "serialVersionUID,id,customerNo,createTime,updateTime,auditEventId,supplement,updateRecord";

        Map<String, Object> data = new HashMap<>(16);

        try {
            Field[] fields = electronicData.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                Object value = field.get(electronicData);
                String name = field.getName();

                data.put(name, value);
            }

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object value = entry.getValue();
                String key = entry.getKey();

                if(value != null && !exclude.contains(key)){
                    JSONObject jsonObject = JSONObject.parseObject(value.toString());
                    entry.setValue(jsonObject);
                }
            }

        } catch (IllegalAccessException e) {
            throw new MobaoException("电子资料明细字段获取异常", e);
        } catch (JSONException e){
            throw new MobaoException("电子资料明细格式异常", e);
        }

        return data;
    }

}

