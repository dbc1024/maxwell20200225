package com.mobao360.audit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.audit.entity.AuditEventDetail;
import com.mobao360.audit.mapper.AuditEventDetailMapper;
import com.mobao360.audit.service.IAuditEventDetailService;
import com.mobao360.system.constant.Constants;
import com.mobao360.system.utils.LoginUserInfoUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-10
 */
@Service
public class AuditEventDetailServiceImpl extends ServiceImpl<AuditEventDetailMapper, AuditEventDetail> implements IAuditEventDetailService {

    /**
     * 根据审核事件ID删除事件明细
     *
     * @param eventId
     * @return
     */
    @Override
    public boolean removeByEventId(Long eventId) {

        LambdaQueryWrapper<AuditEventDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditEventDetail::getAuditEventId, eventId);
        remove(wrapper);

        return remove(wrapper);
    }


    @Override
    public IPage auditDetailPage(IPage page, Map<String, String> params) {

        String auditEventId = params.get("auditEventId");

        LambdaQueryWrapper<AuditEventDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditEventDetail::getAuditEventId, Long.valueOf(auditEventId));
        wrapper.eq(AuditEventDetail::getIfLog, Constants.NO);

        return addNamesByAccount(super.page(page, wrapper));
    }


    @Override
    public IPage logDetailPage(IPage page, Map<String, String> params) {

        String auditEventId = params.get("auditEventId");
        String nodeCode = params.get("nodeCode");

        LambdaQueryWrapper<AuditEventDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AuditEventDetail::getAuditEventId, Long.valueOf(auditEventId));
        wrapper.eq(AuditEventDetail::getNodeCode, nodeCode);
        wrapper.eq(AuditEventDetail::getIfLog, Constants.YES);

        return addNamesByAccount(super.page(page, wrapper));
    }


    private IPage addNamesByAccount(IPage page){
        List<String> accounts = new ArrayList<>();

        List<AuditEventDetail> records = page.getRecords();
        for (AuditEventDetail record : records) {
            accounts.add(record.getOperator());
        }

        Map<String, String> accounts2names = LoginUserInfoUtil.getNamesByAccount(accounts);
        for (AuditEventDetail record : records) {
            record.setOperatorName(accounts2names.get(record.getOperator()));
        }

        return page;
    }
}
