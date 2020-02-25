package com.mobao360.customer.controller;

import com.github.pagehelper.Page;
import com.mobao360.base.BaseController;
import com.mobao360.customer.entity.MerchantConfig;
import com.mobao360.customer.entity.MerchantInfo;
import com.mobao360.customer.service.IMerchantConfigService;
import com.mobao360.customer.service.IMerchantInfoService;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.EndeUtil;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.Result;
import com.mobao360.system.utils.excel.ExcelUtil;
import com.mobao360.system.vo.CertDueExcel;
import com.mobao360.system.vo.NoTradeExcel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@RestController
@RequestMapping("/merchantInfo")
@Log4j2
@Api(tags = "客户-商户基本信息")
public class MerchantInfoController extends BaseController {

    @Autowired
    private IMerchantInfoService merchantInfoService;
    @Autowired
    private IMerchantConfigService merchantConfigService;


    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        Page<Object> page = prePageQuery(params);
        merchantInfoService.pageQuery(params);
        MobaoPage pageResult = MobaoPage.result(page);
        decrypt(pageResult);

        return Result.success(pageResult);
    }


    @ApiOperation("根据客户号查询详情")
    @GetMapping("/detail/{customerNo}")
    public Result detail(@PathVariable String customerNo){

        MerchantInfo merchantInfo = merchantInfoService.getByCustomerNo(customerNo);
        Map<String, Object> merchantInfoMap = DictionaryUtil.keyValueHandle(merchantInfo);
        MerchantConfig config = merchantConfigService.getByCustomerNo(merchantInfo.getCustomerNo());
        merchantInfoMap.put("settStatus", config.getSettlement());
        merchantInfoMap.put("tradeStatus", config.getConsume());

        //按规则解密敏感信息，用于前端展示
        decrypt(merchantInfoMap);
        return Result.success(merchantInfoMap);
    }


    @ApiOperation("证件到期预警分页查询")
    @PostMapping("/certDuePage")
    public Result certDuePage(@RequestBody Map<String, String> params){

        Page<Object> page = prePageQuery(params);
        merchantInfoService.certDuePage(params);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("证件到期预警excel下载")
    @GetMapping("/certDue/download/excel")
    public void certDueExcelDown(@RequestParam Map<String, String> params){

        prePageQuery(params);
        List<CertDueExcel> list = merchantInfoService.certDueExcelDown(params);

        String excelTitle = "证件到期预警";
        String fileName = excelTitle;
        ExcelUtil.exportExcel(list, CertDueExcel.class, excelTitle, fileName);
    }


    @ApiOperation("商户无交易分析报表excel下载")
    @GetMapping("/noTrade/download/excel")
    public void noTradeExcelDown(@RequestParam Map<String, String> params){

        prePageQuery(params);
        List<NoTradeExcel> list = merchantInfoService.noTradeExcelDown(params);

        String excelTitle = "商户无交易分析报表";
        String fileName = excelTitle;
        ExcelUtil.exportExcel(list, NoTradeExcel.class, excelTitle, fileName);
    }


    @ApiOperation(value = "按日,周,月统计商户开户数", notes = "入参{日:day,周:week,月:month}")
    @GetMapping("/count/opened")
    public Result countOpenQuantity(@RequestParam(required = false) String period){

        Map<String, Integer> result = merchantInfoService.countOpenQuantity(period);

        return Result.success(result);
    }


    @ApiOperation("向交易系统推送老数据")
    @GetMapping("/push/{ifAll}")
    public Result pushOldData(@PathVariable String ifAll){

        merchantInfoService.pushOldData(ifAll);

        return Result.success();
    }


    private void decrypt(MobaoPage pageResult){
        List<Object> list = pageResult.getList();
        for (Object object : list) {
            Map<String, Object> map = (Map<String, Object>)object;
            Object legalPersonCertNum = map.get("legalPersonCertNum");
            map.put("legalPersonCertNumEn", EndeUtil.decryptCert(legalPersonCertNum));
        }

    }


    private void decrypt(Map<String, Object> merchantInfo){

        if(merchantInfo == null){
            return;
        }

        //必填项
        Object legalPersonCertNum = merchantInfo.get("legalPersonCertNum");
        //非必填项
        Object contactCertNum = merchantInfo.get("contactCertNum");
        Object shareholderIdNum = merchantInfo.get("shareholderIdNum");
        Object staffCertNum = merchantInfo.get("staffCertNum");



        //全部解密，用于修改页面展示
        merchantInfo.put("legalPersonCertNum", EndeUtil.decrypt(legalPersonCertNum));
        merchantInfo.put("shareholderIdNum", EndeUtil.decrypt(shareholderIdNum));
        merchantInfo.put("contactCertNum", EndeUtil.decrypt(contactCertNum));
        merchantInfo.put("staffCertNum", EndeUtil.decrypt(staffCertNum));



        //部分解密，用于详情页面展示
        merchantInfo.put("legalPersonCertNumEn", EndeUtil.decryptCert(legalPersonCertNum));
        merchantInfo.put("shareholderIdNumEn", EndeUtil.decryptCert(shareholderIdNum));
        merchantInfo.put("contactCertNumEn", EndeUtil.decryptCert(contactCertNum));
        merchantInfo.put("staffCertNumEn", EndeUtil.decryptCert(staffCertNum));

    }

}

