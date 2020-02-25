package com.mobao360.channel.service;

import com.mobao360.channel.entity.ChannelFeeRate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
public interface IChannelFeeRateService extends IService<ChannelFeeRate> {

    /**
     * 新增通道费率
     * @param channelFeeRate
     * @return
     */
    boolean create(ChannelFeeRate channelFeeRate);

    /**
     * 修改通道费率
     * @param channelFeeRate
     * @return
     */
    boolean updateChannelFeeRate(ChannelFeeRate channelFeeRate);

    /**
     * 商户通道费率变更
     * @param channelFeeRate
     * @return
     */
    boolean changeUpdate(ChannelFeeRate channelFeeRate);

    /**
     * 改变通道费率状态
     * @param params
     * @return
     */
    boolean changeStatus(Map<String,String> params);

    /**
     * 通道费率批量激活
     * @param ids
     * @return
     */
    boolean activationBatch(List<Long> ids);


    /**
     * 根据条件获取有效的通道费率
     * @param params
     * @return
     */
    ChannelFeeRate getEffectiveChannelFeeRate(Map<String,String> params);
}
