package com.mobao360.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.base.entity.IndustryInfo;
import com.mobao360.base.mapper.IndustryInfoMapper;
import com.mobao360.base.service.IIndustryInfoService;
import com.mobao360.system.exception.MobaoException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author: CSZ 991587100@qq.com
 * @since 2018-11-23
 */
@Service
public class IndustryInfoServiceImpl extends ServiceImpl<IndustryInfoMapper, IndustryInfo> implements IIndustryInfoService {


    @Override
    public boolean save(IndustryInfo industryInfo){

        IndustryInfo industryInfoOld = getIndustryInfoByCode(industryInfo.getCode());
        if(industryInfoOld != null){
            throw new MobaoException("行业代码已存在");
        }

        return super.save(industryInfo);
    }


    @Override
    public boolean updateById(IndustryInfo industryInfo){

        IndustryInfo industryInfoOld = getIndustryInfoByCode(industryInfo.getCode());
        if(industryInfoOld != null && (!industryInfoOld.getCode().equals(industryInfo.getCode()))){
            throw new MobaoException("行业代码已存在");
        }

        return super.updateById(industryInfo);
    }


    private IndustryInfo getIndustryInfoByCode(String code){

        LambdaQueryWrapper<IndustryInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(IndustryInfo::getCode, code);
        IndustryInfo industryInfo = getOne(wrapper);

        return industryInfo;
    }

}
