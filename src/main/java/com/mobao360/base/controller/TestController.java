package com.mobao360.base.controller;

import com.mobao360.api.feign.SettFeign;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.base.entity.Dictionary;
import com.mobao360.api.feign.TestFeign;
import com.mobao360.system.constant.ConfigConstant;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2018/12/26 09:00
 */
@RestController
@RequestMapping("/test")
@Log4j2
@Api(tags = "测试")
public class TestController {

    @Autowired
    private TestFeign testFeign;

    @Autowired
    private SettFeign settFeign;
    @Autowired
    private ConfigConstant config;


    @ApiOperation("各个超时时间配置测试")
    @GetMapping("/timeout/{millis}")
    public Result testTimeout(@PathVariable Integer millis){

        Result result = settFeign.testTimeout(millis);

        return Result.success(result);
    }


    @ApiOperation("Feign测试，Bean传参")
    @PostMapping("/transBean")
    public Result transBean(@RequestBody AuditEvent auditEvent){

        Result<AuditEvent> result = testFeign.testBean(auditEvent);
        AuditEvent retData = result.getRetData();

        return Result.success(retData);
    }


    @ApiOperation("Feign测试，Map传参")
    @PostMapping("/transMap")
    public Result transMap(@RequestBody Map<String, String> auditEvent){

        Result<AuditEvent> result = testFeign.testMap(auditEvent);
        AuditEvent data = result.getRetData();

        return Result.success(data);
    }


    @ApiOperation(value = "Feign测试，Url传参", notes = "根据‘数据字典类型code’值(如：pay_type)，查询与其对应的所有数据字典键值对")
    @GetMapping("/transUrl/{typeCode}")
    public Result getDic(@PathVariable String typeCode){

        Result result = testFeign.testGetDic(typeCode);
        Object retData = result.getRetData();

        return Result.success(retData);
    }


    @ApiOperation("数字/字符串")
    @PostMapping("/testNumStr")
    public Result testNumStr(@RequestBody Dictionary dictionary){

        return Result.success(dictionary);
    }


}
