package com.mobao360.base.controller;

import com.mobao360.base.entity.IndustryInfo;
import com.mobao360.base.service.IIndustryInfoService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.RegexUtil;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.mobao360.base.BaseController;


import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@RestController
@RequestMapping("/industryInfo")
@Log4j2
@Api(tags = "公共-行业信息")
public class IndustryInfoController extends BaseController {

    @Autowired
    private IIndustryInfoService industryInfoService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = industryInfoService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        IndustryInfo industryInfo = industryInfoService.getById(id);

        return Result.success( DictionaryUtil.keyValueHandle(industryInfo));
    }


    @ApiOperation("新增")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated IndustryInfo industryInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        industryInfoService.save(industryInfo);

        return Result.success();
    }


    @ApiOperation("修改")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated IndustryInfo industryInfo, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        industryInfoService.updateById(industryInfo);

        return Result.success();
    }


    @ApiOperation("删除")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){

        industryInfoService.removeById(id);

        return Result.success();
    }




    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<IndustryInfo> wrapper = new LambdaQueryWrapper<>();
        String industryType = params.get("businessType");
        String code = params.get("code");
        String name = params.get("name");
        String remark = params.get("remark");

        if (StringUtils.isNotBlank(industryType)) {
            wrapper.eq(IndustryInfo::getIndustryType, industryType);
        }
        if (StringUtils.isNotBlank(code)) {
            wrapper.like(IndustryInfo::getCode, code);
        }
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(IndustryInfo::getName, name);
        }
        if (StringUtils.isNotBlank(remark)) {
            wrapper.like(IndustryInfo::getRemark, remark);
        }

        return wrapper;
    }
}

