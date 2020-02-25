package com.mobao360.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.base.BaseController;
import com.mobao360.customer.entity.MerchantFundSettlement;
import com.mobao360.customer.service.IMerchantFundSettlementService;
import com.mobao360.system.utils.DictionaryUtil;
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
 * @since 2019-01-17
 */
@RestController
@RequestMapping("/merchantFundSettlement")
@Log4j2
@Api(tags = "客户-商户即时结算")
public class MerchantFundSettlementController extends BaseController {

    @Autowired
    private IMerchantFundSettlementService merchantFundSettlementService;


    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = merchantFundSettlementService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        MerchantFundSettlement merchantFundSettlement = merchantFundSettlementService.getById(id);

        return Result.success(DictionaryUtil.keyValueHandle(merchantFundSettlement));
    }



    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<MerchantFundSettlement> wrapper = new LambdaQueryWrapper<>();

        String customerNo = params.get("customerNo");

        if(StringUtils.isNotBlank(customerNo)){
            wrapper.eq(MerchantFundSettlement::getCustomerNo, customerNo);
        }

        return wrapper;
    }
}

