package com.mobao360.audit.mapper;

import com.mobao360.audit.entity.AuditEvent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-12-08
 */
@Mapper
public interface AuditEventMapper extends BaseMapper<AuditEvent> {

    /**
     * 通过条件参数进行分页列表查询
     * @param params
     * @return
     */
    List<Map<String, String>> pageQuery(Map<String, Object> params);

    /**
     * 通过审核事件类型 和 审核主体编码 查询审核事件数
     * @param subjectCode 审核主体编码
     * @param auditEventTypes 通过审核事件类型
     * @return 审核事件数
     */
    int countBySubjectCodeAndCAuditCAuditEventType(@Param("subjectCode") String subjectCode, @Param("auditEventTypes") List<String> auditEventTypes);

    /**
     * 统计当前登录人待处理的事件数量
     * @param loginRoles
     * @return
     */
    int countDoing(@Param("loginRoles") List<String> loginRoles);
}
