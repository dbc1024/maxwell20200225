package com.mobao360.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.customer.entity.AgentBasicInfo;
import com.mobao360.customer.mapper.AgentBasicInfoMapper;
import com.mobao360.customer.service.IAgentBasicInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * <p>
 *  service实现类
 * </p>
 *
 * @author ZDX 1628666074@qq.com
 * @since 2019-01-15
 */
@Service
public class AgentBasicInfoServiceImpl extends ServiceImpl<AgentBasicInfoMapper, AgentBasicInfo> implements IAgentBasicInfoService {


	/**
     * 删除
     * @param id
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean removeById(Serializable id){
	
		return super.removeById(id);
	}

	@Override
	public AgentBasicInfo getByAgentNo(String agentNo) {
		LambdaQueryWrapper<AgentBasicInfo> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(AgentBasicInfo::getAgentNo,agentNo);
		return getOne(wrapper);
	}




}