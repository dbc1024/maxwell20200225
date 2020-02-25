package com.mobao360.system.exception;


import com.mobao360.system.utils.Result;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常处理器
 * 
 * @author yhq
 * @email
 * @date 20180726
 */
@RestControllerAdvice
@Log4j2
public class MobaoExceptionHandler {

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(MobaoException.class)
	public Result handleRRException(MobaoException e){
		log.error(e.getMessage(), e);
		return Result.error(e.getCode(), e.getMsg());
	}

	@ExceptionHandler(Exception.class)
	public Result handleException(Exception e){
		log.error(e.getMessage(), e);
		return Result.error();
	}
}
