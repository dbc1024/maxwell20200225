package com.mobao360.api.feign;

import com.mobao360.system.utils.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/2/18 15:25
 */
@FeignClient(name = "mobaopay-descartes")
public interface TradeFeign {

    /**
     * 向交易服务推送开户数据
     * @param data
     * @return
     */
    @PostMapping("/acctManagement/open")
    Result createAccount(@RequestBody Map<String, Object> data);


    /**
     * 向交易服务推送老数据
     * @param data
     * @return
     */
    @PostMapping("/acctManagement/oldSystemOpen")
    Result moveOldData(@RequestBody Map<String, Object> data);


    /**
     * 更新账户状态
     * @param params
     * @return
     */
    @PostMapping("/acctManagement/changeAcctStatus")
    Result updateAccountStatus(@RequestBody Map<String, String> params);

    /**
     * 新增/修改路由
     * @param route
     * @return
     */
    @PostMapping("/trade/saveRoute")
    Result createOrUpdateRoute(@RequestBody Map<String, Object> route);


    /**
     * 查询账户余额
     * @param params
     * @return
     */
    @PostMapping("/acctManagement/queryBal")
    Result queryBal(@RequestBody Map<String, String> params);
}
