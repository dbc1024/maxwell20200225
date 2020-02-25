package com.mobao360.api.feign;

import com.mobao360.audit.entity.AuditEvent;
import com.mobao360.system.config.FeignConfig;
import com.mobao360.system.utils.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2018/12/24 20:32
 */
@FeignClient(name = "mobaopay-maxwell", configuration = FeignConfig.class)
public interface TestFeign {

    /**
     * Feign测试，Bean传参
     * @param event
     * @return
     */
    @PostMapping("/auditEvent/create")
    Result<AuditEvent> testBean (@RequestBody AuditEvent event);


    /**
     * Feign测试，Map传参
     * @param event
     * @return
     */
    @PostMapping("/auditEvent/create")
    Result<AuditEvent> testMap (@RequestBody Map<String, String> event);


    /**
     * Feign测试，Url传参
     * @param typeCode
     * @return
     */
    @GetMapping("/dictionary/keyValue/{typeCode}")
    Result testGetDic (@PathVariable("typeCode") String typeCode);




}
