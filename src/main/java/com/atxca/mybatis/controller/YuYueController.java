package com.atxca.mybatis.controller;

import com.atxca.Util.LogUtil;
import com.atxca.Util.R;
import com.atxca.Util.RandomUtil;
import com.atxca.aop.LogicRuntimeException;
import com.atxca.mybatis.exception.TipException;
import com.atxca.mybatis.service.OrderService;
import com.atxca.mybatis.service.PeriodTimeService;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/yuyue")
public class YuYueController {

    @Autowired
    private OrderService service;

    @Autowired
    private PeriodTimeService timeService;



    /**
     * 根据openid查询待确认订单列表
     */
    @PostMapping(value = "/findOrderListByWaitAndOpenId")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findOrderListByWaitAndOpenId(String openid) {
        LogUtil.d("findOrderListByWaitAndOpenId","openid: "+openid);

        R r = service.findOrderListByWaitAndOpenId(openid);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据openid查询订单列表
     */
    @PostMapping(value = "/findOrderListByOpenId")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findOrderListByOpenId(String openid) {
        LogUtil.d("findOrderListByOpenId","openid: "+openid);

        R r = service.findOrderListByOpenId(openid);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据id删除订单
     */
    @PostMapping(value = "/deleteOrderById")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R deleteOrderById(Integer id) {
        LogUtil.d("deleteOrderById","id: "+id);

        R r = timeService.deleteOrderById(id);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据场馆id查询预约信息
     */
    @PostMapping(value = "/findYuYueListByChangGuanId")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findYuYueListByChangGuanId(Integer id) {
        LogUtil.d("findYuYueListByChangGuanId","id: "+id);

        R r = service.findYuYueListByChangGuanId(id);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据场馆id查询日期剩余场数
     */
    @PostMapping(value = "/findYuYueDateCount")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findYuYueDateCount(Integer id) {
        LogUtil.d("findYuYueDateCount","id: "+id);

        R r = service.findYuYueDateCount(id);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据场馆id和日期查询片场和时间段
     */
    @PostMapping(value = "/findYuYuePianChangList")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findYuYuePianChangList(Integer id,String date) {
        LogUtil.d("findYuYueDateCount","id: "+id);

        R r = service.findYuYuePianChangList(id,date);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 创建预约
     */
    @PostMapping(value = "/createOrder")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R createOrder(String name, String phone, String pids,String reserveTime,Integer vid,String openid,HttpSession session) {
        log.error("createOrderForBack","pids:"+pids+" vid:"+vid+" openid:"+openid+" name:"+name+" phone:"+phone+" reserveTime:"+reserveTime);
        RandomUtil randomUtil = new RandomUtil();
        randomUtil.checkPhoneFormat(phone.trim());
        if(null == reserveTime || "".equals(reserveTime)){
            throw new LogicRuntimeException("预约时间不能为空");
        }
        R r = service.createOrder(name,phone.trim(),pids,reserveTime,vid,openid);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

}
