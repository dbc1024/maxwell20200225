package com.mobao360.channel.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mobao360.channel.entity.ChannelFeeRate;
import com.mobao360.channel.mapper.ChannelFeeRateMapper;
import com.mobao360.channel.service.IChannelFeeRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.system.constant.COperation;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.DateUtils;
import com.mobao360.system.utils.LogUtil;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Service
public class ChannelFeeRateServiceImpl extends ServiceImpl<ChannelFeeRateMapper, ChannelFeeRate> implements IChannelFeeRateService {

    @Autowired
    ChannelFeeRateMapper channelFeeRateMapper;

    /**
     * 通道费率新增
     *
     * @param channelFeeRate
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean create(ChannelFeeRate channelFeeRate) {
        //准备数据
        String today = DateUtil.today();
        String now = DateUtil.now();
        String startDate = channelFeeRate.getStartDate();
        String bankCode = channelFeeRate.getBankCode();
        channelFeeRate.setCreateTime(now);
        channelFeeRate.setUpdateTime(now);

        //检查费率是否已存在于数据库
        checkFeetRepeat(channelFeeRate);
        //判断生效日期是否大于当前时间
        if (!(StringUtils.isNotBlank(startDate) && DateUtils.compare(startDate, today)==1)) {
            throw new MobaoException("生效日期不能为空,且必需大于当前时间.");
        }

        //如果银行不为空 进行分割多条存储
        if (StringUtils.isNotBlank(bankCode)) {
            String[] banks = bankCode.split(",");
            for (String tempBankCode : banks) {
                    channelFeeRate.setBankCode(tempBankCode);
                    save(channelFeeRate);
                }
        }else {
            save(channelFeeRate);
        }

        return true;
    }

    /**
     * 检查费率重复
     * @param channelFeeRate
     */
    private void checkFeetRepeat(ChannelFeeRate channelFeeRate ) {
        String bankCode = channelFeeRate.getBankCode();
        String[] banks = null;
        if (StringUtils.isNotBlank(bankCode)) {
            banks = bankCode.split(",");
        }
        Integer count = channelFeeRateMapper.checkRepeat(channelFeeRate, banks);
        if (count > 0) {
            throw new MobaoException("通道费率已重复");
        }
    }

    /**
     * 修改通道费率
     *
     * @param channelFeeRate
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean updateChannelFeeRate(ChannelFeeRate channelFeeRate) {
        String today = DateUtil.today();
        String startDate = channelFeeRate.getStartDate();

        //获取被修改的正式费率
        ChannelFeeRate formalChannelFeeRate = getById(channelFeeRate.getId());

        //只有状态为初始的通道，才允许修改。且生效时间需要大于当前时间.
        String start = "0";
        if (DateUtils.compare(startDate,today) <= 0 || !start.equals(formalChannelFeeRate.getStatus())){
            throw new MobaoException("只有状态为初始的通道，的通道费率才能进行修改.且新的生效日期必须大于当前时间。");
        }

        String now = DateUtil.now();
        channelFeeRate.setCreateTime(now);
        channelFeeRate.setUpdateTime(now);
        updateById(channelFeeRate);

        //日志记录
        LogUtil.insert("通道费率", COperation.UPDATE, formalChannelFeeRate, channelFeeRate);

        return true;
    }

    /**
     * 通道费率变更
     * @param channelFeeRate
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean changeUpdate(ChannelFeeRate channelFeeRate) {
        //准备数据
        String formalId = channelFeeRate.getFormalId();

        checkFeetRepeat(channelFeeRate);

        if (StringUtils.isBlank(formalId)){
            throw new MobaoException("formalId不能为空");
        }

        //获取被修改的正式费率
        ChannelFeeRate formalChannelFeeRate = getById(formalId);

        String status = formalChannelFeeRate.getStatus();
        String start = "0";

        //修改的原数据状态为活动或停用才允许修改(0-初始；1-激活；2-停用)
        if (start.equals(status)){
            throw new MobaoException("初始状态下的通道费率不允许修改.");
        }
        String startDate = channelFeeRate.getStartDate();
        String today = DateUtil.today();
        //生效时间需要大于当前时间
        if (DateUtils.compare(startDate,today) <= 0){
            throw new MobaoException("生效时间需要大于当前时间.");
        }

        //修改正式数据
        String now = DateUtil.now();
        formalChannelFeeRate.setEndDate(DateUtils.offsetDay(startDate,-1));
        formalChannelFeeRate.setId(Long.parseLong(formalId));
        formalChannelFeeRate.setUpdateTime(now);
        updateById(formalChannelFeeRate);

        channelFeeRate.setCreateTime(now);
        channelFeeRate.setUpdateTime(now);
        save(channelFeeRate);
        return true;
    }

    /**
     * 通道费率的激活和停用
     * @param params
     * @return
     */
    @Override
    public boolean changeStatus(Map<String, String> params) {
        String id = params.get("id");
        String condition = params.get("status");
        String active = "1";
        String down = "2";
        if (StringUtils.isNotBlank(condition) && StringUtils.isNotBlank(id)){
            ChannelFeeRate formalData = getOne(new LambdaQueryWrapper<ChannelFeeRate>().eq(ChannelFeeRate::getId, Long.parseLong(id)));
            if (formalData != null && (active.equals(condition) || down.equals(condition))) {
                formalData.setStatus(condition);
                updateById(formalData);
            }else {
                throw new MobaoException("不存在此用户,或者修改条件不正确，请仔细核对");
            }
        }else {
            throw new MobaoException("条件不能为空.");
        }
        return true;
    }

    /**
     * 批量激活
     * @param ids
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public boolean activationBatch(List<Long> ids) {
        if (ids != null) {
            channelFeeRateMapper.changeStatus(ids);
        }else {
            throw new MobaoException("激活列表为空.");
        }
        return true;
    }


    @Override
    public ChannelFeeRate getEffectiveChannelFeeRate(Map<String, String> params) {

        String channelCode = params.get("channelCode");
        String channelMerchantNo = params.get("channelMerchantNo");
        String payType = params.get("payType");
        String bankCode = params.get("bankCode");

        if(StringUtils.isBlank(channelCode)){
            throw new MobaoException("channelCode不能为空");
        }
        if(StringUtils.isBlank(channelMerchantNo)){
            throw new MobaoException("channelMerchantNo不能为空");
        }
        if(StringUtils.isBlank(payType)){
            throw new MobaoException("payType不能为空");
        }

        ChannelFeeRate feeRate;
        String today = DateUtil.today();

        if(StringUtils.isBlank(bankCode)){
            LambdaQueryWrapper<ChannelFeeRate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ChannelFeeRate::getStatus, "1");
            wrapper.eq(ChannelFeeRate::getChannelCode, channelCode);
            wrapper.eq(ChannelFeeRate::getChannelMerchantNo, channelMerchantNo);
            wrapper.eq(ChannelFeeRate::getPayType, payType);
            wrapper.isNull(ChannelFeeRate::getBankCode);

            wrapper.le(ChannelFeeRate::getStartDate, today);
            wrapper.isNull(ChannelFeeRate::getEndDate);

            feeRate = getOne(wrapper);
            if(feeRate == null){
                LambdaQueryWrapper<ChannelFeeRate> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(ChannelFeeRate::getStatus, "1");
                wrapper2.eq(ChannelFeeRate::getChannelCode, channelCode);
                wrapper2.eq(ChannelFeeRate::getChannelMerchantNo, channelMerchantNo);
                wrapper2.eq(ChannelFeeRate::getPayType, payType);
                wrapper2.isNull(ChannelFeeRate::getBankCode);

                wrapper2.le(ChannelFeeRate::getStartDate, today);
                wrapper2.ge(ChannelFeeRate::getEndDate, today);
                feeRate = getOne(wrapper2);
            }

        }else {
            LambdaQueryWrapper<ChannelFeeRate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ChannelFeeRate::getStatus, "1");
            wrapper.eq(ChannelFeeRate::getChannelCode, channelCode);
            wrapper.eq(ChannelFeeRate::getChannelMerchantNo, channelMerchantNo);
            wrapper.eq(ChannelFeeRate::getPayType, payType);
            wrapper.eq(ChannelFeeRate::getBankCode, bankCode);

            wrapper.le(ChannelFeeRate::getStartDate, today);
            wrapper.isNull(ChannelFeeRate::getEndDate);

            feeRate = getOne(wrapper);
            if(feeRate == null){
                LambdaQueryWrapper<ChannelFeeRate> wrapper2 = new LambdaQueryWrapper<>();
                wrapper2.eq(ChannelFeeRate::getStatus, "1");
                wrapper2.eq(ChannelFeeRate::getChannelCode, channelCode);
                wrapper2.eq(ChannelFeeRate::getChannelMerchantNo, channelMerchantNo);
                wrapper2.eq(ChannelFeeRate::getPayType, payType);
                wrapper2.eq(ChannelFeeRate::getBankCode, bankCode);

                wrapper2.le(ChannelFeeRate::getStartDate, today);
                wrapper2.ge(ChannelFeeRate::getEndDate, today);
                feeRate = getOne(wrapper2);
            }


            //带了bankCode没有查到的情况下，去掉bankCode参数查通用的那一条
            if(feeRate == null){
                LambdaQueryWrapper<ChannelFeeRate> wrapper3 = new LambdaQueryWrapper<>();
                wrapper3.eq(ChannelFeeRate::getStatus, "1");
                wrapper3.eq(ChannelFeeRate::getChannelCode, channelCode);
                wrapper3.eq(ChannelFeeRate::getChannelMerchantNo, channelMerchantNo);
                wrapper3.eq(ChannelFeeRate::getPayType, payType);
                wrapper3.isNull(ChannelFeeRate::getBankCode);

                wrapper3.le(ChannelFeeRate::getStartDate, today);
                wrapper3.isNull(ChannelFeeRate::getEndDate);
                feeRate = getOne(wrapper3);
                if(feeRate == null){
                    LambdaQueryWrapper<ChannelFeeRate> wrapper4 = new LambdaQueryWrapper<>();
                    wrapper4.eq(ChannelFeeRate::getStatus, "1");
                    wrapper4.eq(ChannelFeeRate::getChannelCode, channelCode);
                    wrapper4.eq(ChannelFeeRate::getChannelMerchantNo, channelMerchantNo);
                    wrapper4.eq(ChannelFeeRate::getPayType, payType);
                    wrapper4.isNull(ChannelFeeRate::getBankCode);

                    wrapper4.le(ChannelFeeRate::getStartDate, today);
                    wrapper4.ge(ChannelFeeRate::getEndDate, today);
                    feeRate = getOne(wrapper4);
                }
            }
        }

        return feeRate;
    }
}
