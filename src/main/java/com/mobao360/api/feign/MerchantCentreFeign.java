package com.mobao360.api.feign;

import com.mobao360.system.utils.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/2/15 17:17
 */
@FeignClient(name = "mobaopay-lishizhen")
public interface MerchantCentreFeign {

    /**
     * 向商户中心推送商户中心账号
     * @param account
     * @return
     */
    @PostMapping("/preposition/addAdmin")
    Result createAccount(@RequestBody Map<String, String> aboutAccount);
}
