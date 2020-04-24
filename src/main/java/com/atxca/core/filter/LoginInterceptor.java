package com.atxca.core.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 王志鹏
 * @title: LoginInterceptor
 * @projectName baoge
 * @description: TODO
 * @date 2019/3/26 14:12
 */

@Component
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 预处理回调方法，实现处理器的预处理
     * 返回值：true表示继续流程；false表示流程中断，不会继续调用其他的拦截器或处理器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String tokenClientkey = request.getParameter("token");//获得Token
//        HttpSession session = request.getSession();
//        Object Token = session.getAttribute("token");
//        String tokenServerkey = null;
//
//        if (Token != null) {
//            tokenServerkey = Token.toString();
//        } else {
//            ResponseUtil.write(response, "没有签名不能访问!");
//            return false;
//        }
//
//        System.out.println(tokenServerkey + ": ****");
//        if (!TokenTools.judgeTokenIsEqual(request, "token", "token")) {
//            ResponseUtil.write(response, "签名验证失败!");
//            response.setStatus(403);
//            return false;
//        }

        return true;
    }

    /**
     * 后处理回调方法，实现处理器（Controller）的后处理，但在渲染视图之前
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub
    }

    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，
     * 如性能监控中我们可以在此记录结束时间并输出消耗时间，
     * 还可以进行一些资源清理，类似于try-catch-finally中的finally，
     * 但仅调用处理器执行链中
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO Auto-generated method stub

    }

}