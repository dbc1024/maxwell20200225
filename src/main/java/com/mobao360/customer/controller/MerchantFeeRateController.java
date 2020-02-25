package com.mobao360.customer.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.base.BaseController;
import com.mobao360.customer.entity.MerchantFeeRate;
import com.mobao360.customer.service.IMerchantFeeRateService;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@RestController
@RequestMapping("/merchantFeeRate")
@Log4j2
@Api(tags = "客户-商户费率")
public class MerchantFeeRateController extends BaseController {

    @Autowired
    private IMerchantFeeRateService merchantFeeRateService;


    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params) {

        IPage page = merchantFeeRateService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id) {

        MerchantFeeRate merchantFeeRate = merchantFeeRateService.getById(id);

        return Result.success(DictionaryUtil.keyValueHandle(merchantFeeRate));
    }

    @ApiOperation("未过期费率查询")
    @GetMapping("/activityMerchantFate/{customerNo}")
    public Result activityMerchantFate(@PathVariable String customerNo) {
        List<MerchantFeeRate> merchantFeeRates = merchantFeeRateService.activityMerchantFate(customerNo);
        ArrayList<Map<String, Object>> maps = new ArrayList<>();
        merchantFeeRates.forEach(m->maps.add(DictionaryUtil.keyValueHandle(m)));
        return Result.success(maps);
    }



    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params) {
        LambdaQueryWrapper<MerchantFeeRate> wrapper = new LambdaQueryWrapper<>();

        String customerNo = params.get("customerNo");
        String productKind = params.get("productKind");
        String payType = params.get("payType");
        String overdue = params.get("overdue");

        if (StringUtils.isNotBlank(customerNo)) {
            wrapper.eq(MerchantFeeRate::getCustomerNo, customerNo);
        }
        if (StringUtils.isNotBlank(productKind)) {
            wrapper.eq(MerchantFeeRate::getProductKind, productKind);
        }
        if (StringUtils.isNotBlank(payType)) {
            wrapper.eq(MerchantFeeRate::getPayType, payType);
        }
        if (StringUtils.isNotBlank(overdue)) {

            String now = DateUtil.now();
            //已过期
            if("1".equals(overdue)){
                wrapper.and(i->i.isNotNull(MerchantFeeRate::getEndTime).lt(MerchantFeeRate::getEndTime, now));

            //未过期
            }else if("0".equals(overdue)){
                wrapper.and(i->i.isNull(MerchantFeeRate::getEndTime).or().ge(MerchantFeeRate::getEndTime, now));
            }

        }


        return wrapper;
    }
}

