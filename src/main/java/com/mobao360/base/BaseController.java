package com.mobao360.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mobao360.system.utils.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2018/10/17 14:32
 * @description: 控制层公共父类，集中一些控制层通用处理。
 */
public class BaseController {

    /**
     * 分页参数预处理方法
     */
    public Page<Object> prePageQuery(Map<String, String> params){
        String currPage = params.get("currPage")==null?"1":params.get("currPage");
        String pageSize = params.get("pageSize")==null?"10":params.get("pageSize");
        String cons = params.get("cons");
        if(StringUtils.isNotBlank(cons)){
            params.put("cons", StringUtil.camelToUnderline(cons));
        }
        Page<Object> page = PageHelper.startPage(Integer.parseInt(currPage), Integer.parseInt(pageSize));

        return page;
    }


    /**
     * 分页参数预处理方法（用于处理mybatis_plus原生分页方法）
     */
    public IPage prePageQueryPlus(Map<String, String> params){
        String currPage = params.get("currPage")==null?"1":params.get("currPage");
        String pageSize = params.get("pageSize")==null?"10":params.get("pageSize");
        String cons = params.get("cons");
        String order = params.get("order");

        com.baomidou.mybatisplus.extension.plugins.pagination.Page page = new com.baomidou.mybatisplus.extension.plugins.pagination
                .Page(Integer.parseInt(currPage), Integer.parseInt(pageSize));

        if(StringUtils.isBlank(cons)){
            page.setDesc("id");
        }else {
            cons = StringUtil.camelToUnderline(cons);
            if("asc".equals(order)){
                page.setAsc(cons);
            }else {
                page.setDesc(cons);
            }
        }

        return page;
    }

}
