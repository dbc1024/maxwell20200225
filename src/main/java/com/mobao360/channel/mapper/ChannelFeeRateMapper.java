package com.mobao360.channel.mapper;

import com.mobao360.channel.entity.ChannelFeeRate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Mapper
public interface ChannelFeeRateMapper extends BaseMapper<ChannelFeeRate> {

    /**
     * 批量激活通道费率
     * @param ids
     */
    void changeStatus(@Param("list")List<Long> ids);

    /**
     *
     * @param channelFeeRate
     * @param banks
     * @return
     */
    Integer checkRepeat(@Param("cfr") ChannelFeeRate channelFeeRate,@Param("banks") String[] banks);
}
