package com.mobao360.customer.controller;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mobao360.base.BaseController;
import com.mobao360.customer.entity.MerchantElectronicData;
import com.mobao360.customer.service.IMerchantElectronicDataService;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.DictionaryUtil;
import com.mobao360.system.utils.MobaoPage;
import com.mobao360.system.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-03
 */
@RestController
@RequestMapping("/merchantElectronicData")
@Log4j2
@Api(tags = "客户-商户电子资料")
public class MerchantElectronicDataController extends BaseController {

    @Autowired
    private IMerchantElectronicDataService merchantElectronicDataService;


    @ApiOperation("分页查询")
    @PostMapping("/page")
    public Result page(@RequestBody Map<String, String> params) {

        IPage page = merchantElectronicDataService.page(prePageQueryPlus(params), condition(params));
        DictionaryUtil.keyValueHandle(page);
        MobaoPage pageResult = MobaoPage.result(page);

        return Result.success(pageResult);
    }


    @ApiOperation("根据客户号查询详情")
    @GetMapping("/detail/{customerNo}")
    public Result detail(@PathVariable String customerNo){

        MerchantElectronicData merchantElectronicData = merchantElectronicDataService.getByCustomerNo(customerNo);
        Map<String, Object> electronicData = stringFieldToJsonObject(merchantElectronicData);

        return Result.success(electronicData);
    }


    /**
     * 查询参数预处理方法
     */
    private LambdaQueryWrapper condition(Map<String, String> params) {
        LambdaQueryWrapper<MerchantElectronicData> wrapper = new LambdaQueryWrapper<>();

        String id = params.get("id");

        if (StringUtils.isNotBlank(id)) {
            wrapper.eq(MerchantElectronicData::getId, id);
        }

        return wrapper;
    }


    private Map<String, Object> stringFieldToJsonObject(MerchantElectronicData electronicData){
        if(electronicData==null){
            return null;
        }

        //排除不需要转换的字段
        String exclude = "serialVersionUID,id,customerNo,createTime,updateTime,supplement";

        Map<String, Object> data = new HashMap<>(16);

        try {
            Field[] fields = electronicData.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);

                Object value = field.get(electronicData);
                String name = field.getName();

                data.put(name, value);
            }

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Object value = entry.getValue();
                String key = entry.getKey();

                if(value != null && !exclude.contains(key)){
                    JSONObject jsonObject = JSONObject.parseObject(value.toString());
                    entry.setValue(jsonObject);
                }
            }

        } catch (IllegalAccessException e) {
            throw new MobaoException("电子资料明细字段获取异常", e);
        } catch (JSONException e){
            throw new MobaoException("电子资料明细格式异常", e);
        }

        return data;
    }
}

