package com.mobao360.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mobao360.customer.entity.AgentBasicInfo;

import java.io.Serializable;

/**
 * <p>
 *  service接口
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-15
 */
public interface IAgentBasicInfoService extends IService<AgentBasicInfo> {
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
    boolean removeById(Serializable id);

    /**
     * 查找
     * @param agentNo
     * @return
     */
    AgentBasicInfo getByAgentNo(String agentNo);

}