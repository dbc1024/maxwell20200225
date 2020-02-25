package com.mobao360.api.feign;

import com.mobao360.api.feign.fallback.SettFallBack;
import com.mobao360.system.config.FeignConfig;
import com.mobao360.system.utils.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/5/15 14:40
 */
@FeignClient(name = "mobaopay-newton", fallback = SettFallBack.class, configuration = FeignConfig.class)
public interface SettFeign {

    /**
     * Feign测试，Url传参
     * @param millis
     * @return
     */
    @GetMapping("/home/test/timeout/{millis}")
    Result testTimeout (@PathVariable("millis") Integer millis);
}
