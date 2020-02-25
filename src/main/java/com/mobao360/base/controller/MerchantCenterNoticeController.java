package com.mobao360.base.controller;

import com.mobao360.base.entity.MerchantCenterNotice;
import com.mobao360.base.service.IMerchantCenterNoticeService;

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
 * @since 2019-01-02
 */
@RestController
@RequestMapping("/merchantCenterNotice")
@Log4j2
@Api(tags = "公共-商户中心公告")
public class MerchantCenterNoticeController extends BaseController {

    @Autowired
    private IMerchantCenterNoticeService merchantCenterNoticeService;


    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params) {

        IPage page = merchantCenterNoticeService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id) {

        MerchantCenterNotice merchantCenterNotice = merchantCenterNoticeService.getById(id);

        return Result.success(DictionaryUtil.keyValueHandle(merchantCenterNotice));
    }


    @ApiOperation("新增")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated MerchantCenterNotice merchantCenterNotice, BindingResult bindingResult) {

        RegexUtil.beanValidate(bindingResult);
        merchantCenterNoticeService.save(merchantCenterNotice);

        return Result.success();
    }


    @ApiOperation("修改")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated MerchantCenterNotice merchantCenterNotice, BindingResult bindingResult) {

        RegexUtil.beanValidate(bindingResult);
        merchantCenterNoticeService.updateById(merchantCenterNotice);

        return Result.success();
    }


    @ApiOperation("删除")
    @GetMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {

        merchantCenterNoticeService.removeById(id);

        return Result.success();
    }


    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params) {
        LambdaQueryWrapper<MerchantCenterNotice> wrapper = new LambdaQueryWrapper<>();

        String title = params.get("title");
        String updateTimeStart = params.get("updateTimeStart");
        String updateTimeEnd = params.get("updateTimeEnd");

        if (StringUtils.isNotBlank(title)) {
            wrapper.like(MerchantCenterNotice::getTitle, title);
        }
        if (StringUtils.isNotBlank(updateTimeStart)&&StringUtils.isNotBlank(updateTimeEnd)) {
            wrapper.between(MerchantCenterNotice::getUpdateTime, updateTimeStart,updateTimeEnd);
        }

        return wrapper;
    }
}

