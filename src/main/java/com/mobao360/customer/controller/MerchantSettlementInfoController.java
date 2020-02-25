package com.mobao360.customer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.base.BaseController;
import com.mobao360.customer.entity.MerchantSettlementInfo;
import com.mobao360.customer.service.IMerchantSettlementInfoService;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.EndeUtil;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author CSZ 991587100@qq.com
 * @since 2018-12-25
 */
@RestController
@RequestMapping("/merchantSettlementInfo")
@Log4j2
@Api(tags = "客户-商户结算信息")
public class MerchantSettlementInfoController extends BaseController {

    @Autowired
    private IMerchantSettlementInfoService merchantSettlementInfoService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        IPage page = merchantSettlementInfoService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);

        //按规则解密敏感信息，用于前端展示
        decrypt(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("详情查询")
    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id){

        MerchantSettlementInfo merchantSettlementInfo = merchantSettlementInfoService.getById(id);
        Map<String, Object> settlementInfo = DictionaryUtil.keyValueHandle(merchantSettlementInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(settlementInfo);
        return Result.success(settlementInfo);
    }


    @ApiOperation("根据客户号详查询详情")
    @GetMapping("/detail/customerNo/{customerNo}")
    public Result detailByCustomerNo(@PathVariable String customerNo){

        MerchantSettlementInfo merchantSettlementInfo = merchantSettlementInfoService.getByCustomerNo(customerNo);
        Map<String, Object> settlementInfo = DictionaryUtil.keyValueHandle(merchantSettlementInfo);

        //按规则解密敏感信息，用于前端展示
        decrypt(settlementInfo);
        return Result.success(settlementInfo);
    }


    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params){
        LambdaQueryWrapper<MerchantSettlementInfo> wrapper = new LambdaQueryWrapper<>();

        String customerNo = params.get("customerNo");

        if(StringUtils.isNotBlank(customerNo)){
            wrapper.eq(MerchantSettlementInfo::getCustomerNo, customerNo);
        }

        return wrapper;
    }


    private void decrypt(Map<String, Object> settlementInfo){

        if(settlementInfo == null){
            return;
        }

        Object bankAccountName = settlementInfo.get("bankAccountName");
        Object payeeIdNum = settlementInfo.get("payeeIdNum");
        Object accountNo = settlementInfo.get("accountNo");
        Object reserveMobileNo = settlementInfo.get("reserveMobileNo");

        //全部解密，用于修改页面展示
        settlementInfo.put("bankAccountName", EndeUtil.decrypt(bankAccountName));
        settlementInfo.put("payeeIdNum", EndeUtil.decrypt(payeeIdNum));
        settlementInfo.put("accountNo", EndeUtil.decrypt(accountNo));
        settlementInfo.put("reserveMobileNo", EndeUtil.decrypt(reserveMobileNo));
        //部分解密，用于详情页面展示
        settlementInfo.put("bankAccountNameEn", EndeUtil.decryptName(bankAccountName));
        settlementInfo.put("payeeIdNumEn", EndeUtil.decryptCert(payeeIdNum));
        settlementInfo.put("accountNoEn", EndeUtil.decryptBankCardNo(accountNo));
        settlementInfo.put("reserveMobileNoEn", EndeUtil.decryptTel(reserveMobileNo));

    }

    private void decrypt(IPage page){

        if(page == null){
            return;
        }

        List<Map<String, Object>> records = page.getRecords();
        if(records == null || records.size() == 0){
            return;
        }

        for (Map<String, Object> record : records) {
            decrypt(record);
        }
    }
}

