package com.mobao360.api.feign.fallback;

import com.mobao360.api.feign.SettFeign;
import com.mobao360.system.utils.Result;
import org.springframework.stereotype.Component;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2019/5/15 16:06
 */
@Component
public class SettFallBack implements SettFeign {

    @Override
    public Result testTimeout(Integer millis) {
        return Result.error("太拥挤了，请稍后再试.....");
    }
}
