/**
 * Copyright 2018 人人开源 http://www.renren.io
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.mobao360.system.utils;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author: CSZ 991587100@qq.com
 * @date: 2018/10/17 11:27
 * @description: 分页结果封装工具类
 */
public class MobaoPage{

	/**
	 * 总记录数
	 */
	private long totalCount;
	/**
	 * 总页数
	 */
	private long totalPage;
	/**
	 * 每页条数
	 */
	private long pageSize;
	/**
	 * 页码
	 */
	private long currPage;
	/**
	 * 列表数据
	 */
	private List<Object> list;




	public static MobaoPage result(Page<Object> page){
		PageInfo pageInfo = new PageInfo<>(page.getResult());

		MobaoPage mobaoPage = new MobaoPage();
		mobaoPage.setTotalCount(pageInfo.getTotal());
		mobaoPage.setTotalPage(pageInfo.getPages());
		mobaoPage.setCurrPage(pageInfo.getPageNum());
		mobaoPage.setList(pageInfo.getList());
		mobaoPage.setPageSize(pageInfo.getPageSize());

		return mobaoPage;
	}

	public static MobaoPage result(IPage page){

		MobaoPage mobaoPage = new MobaoPage();
		mobaoPage.setTotalCount(page.getTotal());
		mobaoPage.setTotalPage(page.getPages());
		mobaoPage.setCurrPage(page.getCurrent());
		mobaoPage.setList(page.getRecords());
		mobaoPage.setPageSize(page.getSize());

		return mobaoPage;
	}


	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getCurrPage() {
		return currPage;
	}

	public void setCurrPage(long currPage) {
		this.currPage = currPage;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}
}
