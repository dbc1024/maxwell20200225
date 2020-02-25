package com.mobao360.api.feign;

import com.mobao360.system.utils.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/8/27 14:37
 */
@FeignClient(name = "mobaopay-gauss")
public interface PersonalFeign {

    /**
     * 修改个人客户的状态
     * @param data
     * @return
     */
    @PostMapping("/app/updateCustomerStatus")
    Result updateCustomerStatus(@RequestBody Map data);
}
