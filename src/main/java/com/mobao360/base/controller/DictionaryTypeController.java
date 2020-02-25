package com.mobao360.base.controller;

import com.mobao360.base.entity.DictionaryType;
import com.mobao360.base.service.IDictionaryTypeService;

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
@RequestMapping("/dictionaryType")
@Log4j2
@Api(tags = "公共-数据字典类型")
public class DictionaryTypeController extends BaseController {

    @Autowired
    private IDictionaryTypeService dictionaryTypeService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = dictionaryTypeService.page(prePageQueryPlus(params), condition(params));
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }






    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<DictionaryType> wrapper = new LambdaQueryWrapper<>();

        String code = params.get("code");
        String name = params.get("name");
        String remark = params.get("remark");

        if(StringUtils.isNotBlank(code)){
            wrapper.eq(DictionaryType::getCode, code);
        }
        if(StringUtils.isNotBlank(name)){
            wrapper.like(DictionaryType::getName, name);
        }
        if(StringUtils.isNotBlank(remark)){
            wrapper.like(DictionaryType::getRemark, remark);
        }
        return wrapper;
    }


}

