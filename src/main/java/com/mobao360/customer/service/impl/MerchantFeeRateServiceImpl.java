package com.mobao360.customer.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.customer.entity.MerchantFeeRate;
import com.mobao360.customer.mapper.MerchantFeeRateMapper;
import com.mobao360.customer.service.IMerchantFeeRateService;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.NetUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Service
public class MerchantFeeRateServiceImpl extends ServiceImpl<MerchantFeeRateMapper, MerchantFeeRate> implements IMerchantFeeRateService {


    /**
     * 根据客户号，支付方式查询当前可用的费率
     * @param customerNo
     * @param payType
     * @return
     */
    @Override
    public MerchantFeeRate getByCustomerNoAndPayType(String customerNo, String payType) {

        if(StringUtils.isBlank(customerNo)){
            customerNo = NetUtil.getCustomerNoFromHeader();
            if(StringUtils.isBlank(customerNo)){
                throw new MobaoException("客户号不能为空");
            }
        }

        MerchantFeeRate feeRate;

        String now = DateUtil.now();

        LambdaQueryWrapper<MerchantFeeRate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MerchantFeeRate::getCustomerNo, customerNo);
        wrapper.eq(MerchantFeeRate::getPayType, payType);
        wrapper.le(MerchantFeeRate::getStartTime, now);
        wrapper.isNull(MerchantFeeRate::getEndTime);

        feeRate = getOne(wrapper);
        if(feeRate == null){
            LambdaQueryWrapper<MerchantFeeRate> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(MerchantFeeRate::getCustomerNo, customerNo);
            wrapper2.eq(MerchantFeeRate::getPayType, payType);
            wrapper2.le(MerchantFeeRate::getStartTime, now);
            wrapper2.gt(MerchantFeeRate::getEndTime, now);
            feeRate = getOne(wrapper2);
        }

        return feeRate;
    }

    /**
     * 根据客户号查询未过期的商户费率
     * @param customerNo
     * @return
     */
    @Override
    public List<MerchantFeeRate> activityMerchantFate(String customerNo) {
        if (customerNo == null || "".equals(customerNo)) {
            throw new MobaoException("客户号不能为空");
        }
        String now = DateUtil.now();
        //查询条件构造
        LambdaQueryWrapper<MerchantFeeRate> wrapper = new LambdaQueryWrapper<MerchantFeeRate>()
                .eq(MerchantFeeRate::getCustomerNo, customerNo)
                .and(i->i.isNull(MerchantFeeRate::getEndTime).or().gt(MerchantFeeRate::getEndTime, now));

        return list(wrapper);
    }
}
