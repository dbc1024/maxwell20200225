package com.mobao360.customer.controller;

import com.mobao360.customer.entity.Salesman;
import com.mobao360.customer.service.ISalesmanService;

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
 * @author CSZ 991587100@qq.com
 * @since 2019-01-14
 */
@RestController
@RequestMapping("/salesman")
@Log4j2
@Api(tags = "客户-销售基本信息")
public class SalesmanController extends BaseController {

    @Autowired
    private ISalesmanService salesmanService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = salesmanService.page(prePageQueryPlus(params), condition(params));
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        Salesman salesman = salesmanService.getById(id);

        return Result.success(salesman);
    }


    @ApiOperation("新增")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated Salesman salesman, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        salesmanService.save(salesman);

        return Result.success(salesman);
    }


    @ApiOperation("修改")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated Salesman salesman, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        salesmanService.updateById(salesman);

        return Result.success(salesman);
    }


    @ApiOperation("删除")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){

        salesmanService.removeById(id);

        return Result.success();
    }




    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<Salesman> wrapper = new LambdaQueryWrapper<>();

        String salesNo = params.get("salesNo");
        String name = params.get("name");
        String tel = params.get("tel");
        if(StringUtils.isNotBlank(salesNo)){
            wrapper.eq(Salesman::getSalesNo, salesNo);
        }
        if(StringUtils.isNotBlank(name)){
            wrapper.like(Salesman::getName, name);
        }
        if(StringUtils.isNotBlank(tel)){
            wrapper.eq(Salesman::getTel, tel);
        }
        return wrapper;
    }
}

