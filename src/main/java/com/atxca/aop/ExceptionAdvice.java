package com.atxca.aop;

import com.atxca.Util.R;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 黄滚
 * Created by hg at 2020/1/26 2:30 下午
 */
@Slf4j
@ControllerAdvice
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R process(Throwable cause, Exception e, HttpServletRequest request, HttpServletResponse response) {
//        response.addHeader("Access-Control-Allow-Methods", "OPTION,OPTIONS,GET,POST,PATCH,DELETE");
//        response.addHeader("Access-Control-Allow-Headers", "authorization,rid,Authorization,Content-Type,Accept,x-requested-with,Locale");
//        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        log.error("cause:{}，exception:{},uri:{},host:{},remote:{},method:{}",
                cause.getMessage(),e.getMessage(),
                request.getRequestURI(),request.getServerName(),
                request.getRemoteHost(),request.getMethod().toUpperCase());
        log.error("exception:{}",e.getMessage());

            e.printStackTrace();
            cause.printStackTrace();

        if(e.getClass() == MissingServletRequestParameterException.class || e.getClass() == LogicRuntimeException.class || e.getClass() ==IllegalArgumentException.class){
            return  R.fail(e.getMessage());
        }else {
            return R.fail("操作异常，请联系客服！");
        }
    }
}
