package com.mobao360.api.feign;

import com.mobao360.system.utils.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/3/18 14:10
 */
@FeignClient(name = "mobaopay-tesla")
public interface RiskFeign {

    /**
     * 黑名单校验
     * @param params
     * @return
     */
    @PostMapping("/blacklist/CheckBlacklist")
    Result blacklistCheck (@RequestBody Map<String, String> params);

    /**
     * 为风控系统推送新入网的客户号
     * @param params
     * @return
     */
    @PostMapping("/riskGrade/merchInnet")
    Result pushCustomerNo (@RequestBody Map<String, String> params);
}
