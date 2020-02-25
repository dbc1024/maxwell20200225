package com.mobao360.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import com.mobao360.base.BaseController;
import com.mobao360.base.mapper.OtherMapper;
import com.mobao360.customer.entity.MerchantCentreAccount;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.service.IMerchantCentreAccountService;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.system.constant.Constants;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.Result;
import com.mobao360.task.InitTaskData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author CSZ 991587100@qq.com
 * @since 2019-01-18
 */
@RestController
@RequestMapping("/other")
@Log4j2
@Api(tags = "公共-其他")
public class OtherController extends BaseController {

    @Autowired
    private IMerchantInfoService merchantInfoService;
    @Autowired
    private IMerchantCentreAccountService merchantCentreAccountService;

    @Autowired
    private OtherMapper otherMapper;


    @ApiOperation("MCC分页查询")
    @PostMapping("/mcc/page")
    public Result mccPage(@RequestBody Map<String, String> params){

        Page<Object> page = prePageQuery(params);
        otherMapper.mccPageQuery(params);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("微信经营类目分页查询")
    @PostMapping("/wx/page")
    public Result wxPage(@RequestBody Map<String, String> params){

        Page<Object> page = prePageQuery(params);
        otherMapper.wxPageQuery(params);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("行业列表查询")
    @PostMapping("/industry/list")
    public Result industryPage(@RequestBody Map<String, String> params){

        List<Map<String, String>> industryList = otherMapper.industryList(params);

        return Result.success(industryList);
    }

    @ApiOperation("网联业务种类列表查询")
    @PostMapping("/nucc/list")
    public Result nuccList(){

        List<Map<String, String>> nuccList = otherMapper.nuccList();

        return Result.success(nuccList);
    }


    @ApiOperation("总行联行列表查询")
    @PostMapping("/headUnionBank/list")
    public Result headUnionBankList(@RequestParam(required = false) String bankName){

        List<Map<String, String>> headUnionBankList = otherMapper.headUnionBankList(bankName);

        return Result.success(headUnionBankList);
    }


    @ApiOperation("开户行下拉列表查询")
    @PostMapping("/openingBank/list")
    public Result openingBankList(@RequestParam(required = false) String bankName){

        List<Map<String, String>> openingBankList = otherMapper.openingBankList(bankName);

        return Result.success(openingBankList);
    }


    @ApiOperation("省下拉列表查询")
    @PostMapping("/province/list")
    public Result provinceList(){

        List<Map<String, String>> provinceList = otherMapper.provinceList();

        return Result.success(provinceList);
    }


    @ApiOperation("市下拉列表查询")
    @PostMapping("/city/list")
    public Result cityList(@RequestParam String provinceCode){

        List<Map<String, String>> cityList = otherMapper.cityList(provinceCode);

        return Result.success(cityList);
    }


    @ApiOperation(value = "分支行下拉列表查询", notes = "入参{bankNo，cityCode，unionBankName}")
    @PostMapping("/branchBank/list")
    public Result branchBankList(@RequestBody Map<String, Object> params){

//        String areaCode = params.get("cityCode").toString();
//        List<String> areaCodeList = otherMapper.districtCodeList(cityCode);
//        areaCodeList.add(cityCode);

//        params.put("areaCodeList", areaCodeList);

        List<Map<String, String>> branchBankList = otherMapper.branchBankList(params);

        return Result.success(branchBankList);
    }


    /**
     * 手动重启所有定时任务
     * (以防测试需要，避免重启服务)
     */
    @ApiOperation("手动启动所有定时任务")
    @GetMapping("/startAllTask")
    public Result startAllTask(){

        LambdaQueryWrapper<MerchantInfo> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.ne(MerchantInfo::getTask1, Constants.YES);
        wrapper1.isNull(MerchantInfo::getOldCustomerNo);
        int count1 = merchantInfoService.count(wrapper1);
        InitTaskData.tradeAccountData.addAndGet(count1);

        LambdaQueryWrapper<MerchantCentreAccount> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.ne(MerchantCentreAccount::getTask1, Constants.YES);
        int count2 = merchantCentreAccountService.count(wrapper2);
        InitTaskData.merchantCentreAccount.addAndGet(count2);

        return Result.success();
    }

}

