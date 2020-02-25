package com.mobao360.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mobao360.base.mapper.BankCodeMapper;
import com.mobao360.base.entity.BankCode;
import com.mobao360.base.mapper.OtherMapper;
import com.mobao360.base.service.IBankCodeService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 银行编码表 service实现类
 * </p>
 *
 * @author CSZ 991587100@qq.com
 * @since 2019-01-03
 */
@Service
public class BankCodeServiceImpl extends ServiceImpl<BankCodeMapper, BankCode> implements IBankCodeService {

	@Autowired
	private OtherMapper otherMapper;

    /**
     * 新增
     * @param bankCode
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean save(BankCode bankCode){
	
		return super.save(bankCode);
	}
	
	/**
     * 修改
     * @param bankCode
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean updateById(BankCode bankCode){
	
		return super.updateById(bankCode);
	}
	
	/**
     * 删除
     * @param id
     * @return
     */
    @Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor=Exception.class)
    public boolean removeById(Serializable id){
	
		return super.removeById(id);
	}

	@Override
	public BankCode getCodeByBranchBankNo(String branchBankNo) {

		String headUnionBankNo = otherMapper.getHeadByBranch(branchBankNo);

		LambdaQueryWrapper<BankCode> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BankCode::getHeadUnionBankNo, headUnionBankNo);

		return getOne(wrapper);
	}

	@Override
	public BankCode getByBankCode(String bankCode) {

		LambdaQueryWrapper<BankCode> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(BankCode::getBankCode, bankCode);

		return getOne(wrapper);
	}

	@Override
	public Map<String, String> getKeyValue() {

		Map<String, String> keyValueMap = new HashMap<>(16);

		List<BankCode> list = list(null);
		for (BankCode bankCode : list) {
			keyValueMap.put(bankCode.getBankCode(), bankCode.getBankName());
		}

		return keyValueMap;
	}


}