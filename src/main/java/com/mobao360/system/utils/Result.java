package com.mobao360.system.utils;


import com.mobao360.system.constant.Constants;
import lombok.Data;

import java.io.Serializable;

/**
 * 输出结果封装类
 * @author: CSZ 991587100@qq.com
 * @date: 2018/12/25 13:47
 */
@Data
public class Result<T> implements Serializable {
	
	private String retCode;

	private String retMsg;

	private String serName;

	private T retData;


	public static <T> Result<T> success(){

		Result<T> result = new Result<>();
		//成功统一标识码:1
		result.setRetCode(Constants.YES);
		//成功默认信息：success
		result.setRetMsg(Constants.DEFAULT_SUCCESS_MSG);
		//服务名：maxwell
		result.setSerName(Constants.SERVER_NAME);

		return result;
	}

	public static <T> Result<T> success(T data){

		Result<T> result = success();
		result.setRetData(data);

		return result;
	}


	public static <T> Result<T> success(String msg){

		Result<T> result = success();
		result.setRetMsg(msg);

		return result;
	}

	public static <T> Result<T> success(String msg, T data){

		Result<T> result = success();
		result.setRetMsg(msg);
		result.setRetData(data);

		return result;
	}


	public static <T> Result<T> error(){

		Result<T> result = new Result<>();
		//失败默认标识码:0
		result.setRetCode(Constants.NO);
		//失败默认信息
		result.setRetMsg(Constants.DEFAULT_FAIL_MSG);
		//服务名：maxwell
		result.setSerName(Constants.SERVER_NAME);

		return result;
	}


	public static <T> Result<T> error(String msg){

		Result<T> result = error();
		result.setRetMsg(msg);

		return result;
	}


	public static <T> Result<T> error(String code, String msg){

		Result<T> result = error();
		result.setRetCode(code);
		result.setRetMsg(msg);

		return result;
	}

}
