package com.mobao360.channel.controller;

import cn.hutool.core.date.DateUtil;
import com.mobao360.base.service.IBankCodeService;
import com.mobao360.channel.entity.ChannelFeeRate;
import com.mobao360.channel.service.IChannelFeeRateService;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.system.constant.Constants;
import com.mobao360.system.utils.*;
import com.netflix.hystrix.metric.CachedValuesHistogram;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.mobao360.base.BaseController;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@RestController
@RequestMapping("/channelFeeRate")
@Log4j2
@Api(tags = "通道-通道费率")
public class ChannelFeeRateController extends BaseController {

    @Autowired
    private IChannelFeeRateService channelFeeRateService;
    @Autowired
    private IBankCodeService bankCodeService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = channelFeeRateService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.putKeyValueMap("bank_code", bankCodeService.getKeyValue());
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        ChannelFeeRate channelFeeRate = channelFeeRateService.getById(id);
        DictionaryUtil.putKeyValueMap("bank_code", bankCodeService.getKeyValue());

        return Result.success(DictionaryUtil.keyValueHandle(channelFeeRate));
    }


    @ApiOperation("新增")
    @PostMapping("/create")
    public Result create(@RequestBody @Validated ChannelFeeRate channelFeeRate, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        channelFeeRateService.create(channelFeeRate);

        return Result.success();
    }


    @ApiOperation("修改")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated ChannelFeeRate channelFeeRate, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        channelFeeRateService.updateChannelFeeRate(channelFeeRate);

        return Result.success();
    }

    @ApiOperation("变更")
    @PostMapping("/changeUpdate")
    public Result changeUpdate(@RequestBody @Validated ChannelFeeRate channelFeeRate, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        channelFeeRateService.changeUpdate(channelFeeRate);

        return Result.success();
    }

    /**
     *  入参{id:通道费率id ; status ：状态}
     * @param params
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "状态改变",notes = "通道费率的激活，停用均调用此接口.")
    @PostMapping("/changeStatus")
    public Result changeStatus(@RequestBody @Validated Map<String,String> params, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        channelFeeRateService.changeStatus(params);

        return Result.success();
    }

    /**
     * 入参 ：修改通道费率id集
     * @param ids
     * @param bindingResult
     * @return
     */
    @ApiOperation("批量激活")
    @PostMapping("/activationBatch")
    public Result activationBatch(@RequestBody @Validated List<Long> ids, BindingResult bindingResult){

        RegexUtil.beanValidate(bindingResult);
        channelFeeRateService.activationBatch(ids);

        return Result.success();
    }





    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<ChannelFeeRate> wrapper = new LambdaQueryWrapper<>();
        String channelCode = params.get("channelCode");
        String channelMerchantNo = params.get("channelMerchantNo");
        String status = params.get("status");
        String productKind = params.get("productKind");
        String payType = params.get("payType");
        String bankCode = params.get("bankCode");
        //是否过期标志
        String overdue = params.get("overdue");
        //通道号
        if (StringUtils.isNotBlank(channelCode)){
            wrapper.eq(ChannelFeeRate::getChannelCode,channelCode);
        }
        //通道商户号
        if (StringUtils.isNotBlank(channelMerchantNo)){
            wrapper.eq(ChannelFeeRate::getChannelMerchantNo,channelMerchantNo);
        }
        if (StringUtils.isNotBlank(status)){
            wrapper.eq(ChannelFeeRate::getStatus,status);
        }
        //产品类型
        if (StringUtils.isNotBlank(productKind)){
            wrapper.eq(ChannelFeeRate::getProductKind,productKind);
        }
        //支付方式
        if (StringUtils.isNotBlank(payType)){
            wrapper.eq(ChannelFeeRate::getPayType,payType);
        }

        if (StringUtils.isNotBlank(overdue)) {

            String today = DateUtil.today();
            //已过期
            if("1".equals(overdue)){
                wrapper.and(i->i.isNotNull(ChannelFeeRate::getEndDate).lt(ChannelFeeRate::getEndDate, today));

                //未过期
            }else if("0".equals(overdue)){
                wrapper.and(i->i.isNull(ChannelFeeRate::getEndDate).or().ge(ChannelFeeRate::getEndDate, today));
            }

        }

        //银行
        if (StringUtils.isNotBlank(bankCode)){
            wrapper.eq(ChannelFeeRate::getBankCode,bankCode);
        }

        return wrapper;
    }
}

