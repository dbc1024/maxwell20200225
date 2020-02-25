package com.mobao360.system.exception;

import com.mobao360.system.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 自定义异常
 * 
 * @author yhq
 * @email
 * @date 20180726
 */
public class MobaoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private String msg;
	private String code = Constants.NO;

	public MobaoException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public MobaoException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public MobaoException(String msg, String code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public MobaoException(String msg, String code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	/**
	 * 获取详细的异常信息
	 * @param e
	 * @return
	 */
	public static String detail(Throwable e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		e.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}
}
