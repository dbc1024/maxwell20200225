package com.mobao360.api.feign;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/2/13 16:12
 */

import com.mobao360.system.utils.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@FeignClient(name = "mobaopay-uias")
public interface UserFeign {

    /**
     * 根据userId获取用户的角色集
     * @param userId
     * @return
     */
    @GetMapping("/permission/role/roles")
    Result<List<HashMap<String, String>>> getUserRoles (@RequestParam("userId") String userId);

    /**
     * 根据用户账号集合，获取用户名集合
     * @param userAccounts
     * @return
     */
    @PostMapping("/permission/sysUser/batchGetNameByUsername")
    Result<HashMap<String, String>> getNamesByAccount (@RequestBody List<String> userAccounts);


    /**
     * 注销商户
     * @param logout
     * @return
     */
    @PostMapping("/permission/sysUser/cancelMerchantAccount")
    Result logout(@RequestBody Map logout);

}
