package com.mobao360.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.audit.entity.AuditEventFlow;
import com.mobao360.audit.mapper.AuditEventFlowMapper;
import com.mobao360.audit.service.IAuditEventFlowService;
import com.mobao360.audit.service.IAuditEventService;
import com.mobao360.system.constant.CAuditEventStatus;
import com.mobao360.system.constant.COperation;
import com.mobao360.system.exception.MobaoException;
import com.mobao360.system.utils.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-10
 */
@Service
public class AuditEventFlowServiceImpl extends ServiceImpl<AuditEventFlowMapper, AuditEventFlow> implements IAuditEventFlowService {

    @Autowired
    private AuditEventFlowMapper auditEventFlowMapper;

    @Autowired
    private IAuditEventService auditEventService;

    @Override
    public AuditEventFlow getNodeInfoByTypeAndNodeCode(String auditEventType, String nodeCode) {

        LambdaQueryWrapper<AuditEventFlow> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(AuditEventFlow::getAuditEventType, auditEventType);
        wrapper.eq(AuditEventFlow::getNodeCode, nodeCode);
        wrapper.isNotNull(AuditEventFlow::getOperatorRole);

        AuditEventFlow flow = getOne(wrapper);

        return flow;
    }

    @Override
    public AuditEventFlow getNextNodeInfoByTypeAndNodeCode(String auditEventType, String nodeCode) {

        return auditEventFlowMapper.getNextNodeInfoByTypeAndNodeCode(auditEventType, nodeCode);
    }

    @Override
    public AuditEventFlow getLastNodeInfoByTypeAndNodeCode(String auditEventType, String nodeCode) {

        return auditEventFlowMapper.getLastNodeInfoByTypeAndNodeCode(auditEventType, nodeCode);
    }

    @Override
    public String getMaxNodeCodeByType(String auditEventType) {

        return auditEventFlowMapper.getMaxNodeCodeByType(auditEventType);
    }

    /**
     * 新增
     * 审核流程中，各节点的角色不能重复
     * @param flowList
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(List<AuditEventFlow> flowList) {

        // 审核流程中，各节点的角色不能重复
        Set<String> roleSet = new HashSet<>();
        for (AuditEventFlow flow : flowList) {
            roleSet.add(flow.getOperatorRole());
        }
        if(roleSet.size()<flowList.size()){
            throw new MobaoException("同一审核流程中不能有重复的角色");
        }
        saveBatch(flowList);
        LogUtil.insert("审核流程管理", COperation.CREATE, null, flowList);

        return true;
    }

    /**
     * 修改
     * 备注：批量修改，实际上是将同一事件流程的数据全部删除，再保存整个列表的数据
     * @param flowList
     * @return
     */
    @Override
    @Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean update(List<AuditEventFlow> flowList) {

        // 审核流程中，各节点的角色不能重复
        Set<String> roleSet = new HashSet<>();
        for (AuditEventFlow flow : flowList) {
            roleSet.add(flow.getOperatorRole());

        }
        if(roleSet.size()<flowList.size()){
            throw new MobaoException("同一审核流程中不能有重复的角色");
        }

        String auditEventType = flowList.get(0).getAuditEventType();
        int num = countAuditingEventByType(auditEventType);
        if(num > 0){
            throw new MobaoException("该流程有事件正在审核中，不可修改");
        }

        //同一事件流程的数据全部删除
        remove(new LambdaQueryWrapper<AuditEventFlow>().eq(AuditEventFlow::getAuditEventType, auditEventType));

        // 这里不能用批量插入,同一事务中不能有单条操作和批量操作
        for (AuditEventFlow flow : flowList) {
            save(flow);
        }

        LogUtil.insert("审核流程管理", COperation.UPDATE, null, flowList);

        return true;
    }


    /**
     * 统计正在审核中的某种事件的数量
     */
    private int countAuditingEventByType(String type){

        LambdaQueryWrapper<AuditEvent> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(AuditEvent::getStatus, new Object[]{CAuditEventStatus.DRAFT, CAuditEventStatus.AUDITING});
        wrapper.eq(AuditEvent::getAuditEventType, type);

        int count = auditEventService.count(wrapper);

        return count;
    }
}
