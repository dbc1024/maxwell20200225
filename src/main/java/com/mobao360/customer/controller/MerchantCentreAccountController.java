package com.mobao360.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.base.BaseController;
import com.mobao360.customer.entity.MerchantCentreAccount;
import com.mobao360.customer.service.IMerchantCentreAccountService;
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
 * @since 2018-12-28
 */
@RestController
@RequestMapping("/merchantCentreAccount")
@Log4j2
@Api(tags = "客户-商户中心账号")
public class MerchantCentreAccountController extends BaseController {

    @Autowired
    private IMerchantCentreAccountService merchantCentreAccountService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = merchantCentreAccountService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("根据客户号查询详情")
    @GetMapping("/detail/{customerNo}")
    public Result detail(@PathVariable String customerNo){

        MerchantCentreAccount merchantCentreAccount = merchantCentreAccountService.getByCustomerNo(customerNo);

        return Result.success(merchantCentreAccount);
    }





    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<MerchantCentreAccount> wrapper = new LambdaQueryWrapper<>();

        String id = params.get("id");

        if(StringUtils.isNotBlank(id)){
            wrapper.eq(MerchantCentreAccount::getId, id);
        }

        return wrapper;
    }
}

