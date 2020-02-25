package com.mobao360.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.mobao360.base.BaseController;
import com.mobao360.base.entity.Dictionary;
import com.mobao360.base.service.IDictionaryService;
import com.mobao360.base.service.IDictionaryTypeService;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@RestController
@RequestMapping("/dictionary")
@Log4j2
@Api(tags = "公共-数据字典")
public class DictionaryController extends BaseController {

    @Autowired
    private IDictionaryService dictionaryService;



    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params){

        params.put("cons", "code");
        Page<Object> page = prePageQuery(params);
        dictionaryService.pageQuery(params);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation(value = "数据字典键值对查询", notes = "根据‘数据字典类型code’值(如：pay_type)，查询与其对应的所有数据字典键值对")
    @GetMapping("/keyValue/{typeCode}")
    public Result getKeyValueListByCode(@PathVariable String typeCode) {

        List<Map<String, String>> keyValueList = DictionaryUtil.getKeyValueListByCode(typeCode);

        return Result.success(keyValueList);
    }


    @ApiOperation(value = "根据产品获取产品下的所有支付方式")
    @GetMapping("/payTypes/{productKind}")
    public Result getPayTypeListByProduct(@PathVariable String productKind) {

        List<Map<String, String>> payTypeList = DictionaryUtil.getKeyValueListByCode("pay_type");
        String kind = productKind.substring(0, 2);

        List<Map<String, String>> payTypesInKind = new LinkedList<>();
        for (Map<String, String> payType : payTypeList) {
            if(kind.equals(payType.get("value").substring(0, 2))){
                payTypesInKind.add(payType);
            }
        }

        return Result.success(payTypesInKind);
    }


    @ApiOperation(value = "数据字典缓存清理", notes = "用于数据库手动导入数据时，更新程序中数据字典缓存。")
    @GetMapping("/cleanCache/{typeCode}")
    public Result cleanDictionaryCache(@PathVariable String typeCode) {

        dictionaryService.cleanDictionaryCache(typeCode);

        return Result.success();
    }

}

