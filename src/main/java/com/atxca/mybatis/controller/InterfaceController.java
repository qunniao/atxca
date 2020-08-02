package com.atxca.mybatis.controller;

import com.atxca.Util.LogUtil;
import com.atxca.Util.R;
import com.atxca.mybatis.exception.TipException;
import com.atxca.mybatis.service.PeriodTimeService;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/front")
public class InterfaceController {

    @Autowired
    private PeriodTimeService service;


    /**
     * 根据场馆id获取时间段
     */
    @PostMapping(value = "/findPeriodTimeListById")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findPeriodTimeListById(Integer id) {
        LogUtil.d("findPeriodTimeListById","id: "+id);

        R r = service.findPeriodTimeListById(id);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据日期和时间段获取场地列表
     */
    @PostMapping(value = "/findChangDiList")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findChangDiList(String date, String time, Integer changguan_type) {
        LogUtil.d("findChangDiList","date: "+date+" time:"+time+" changguan_type:"+changguan_type);

        R r = service.findChangDiList(date,time,changguan_type);
        return r;
    }

    /**
     * 根据日期和时间段修改片场状态
     */
    @PostMapping(value = "/updatePianChangState")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R updatePianChangState(String date,String time,Integer id,Integer old_state,Integer new_state) {
        LogUtil.d("updatePianChangState","date: "+date+" time:"+time+" id:"+id+" old_state:"+old_state+" new_state:"+new_state);

        R r = service.updatePianChangState(date,time,id,old_state,new_state);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 获取场馆类型
     */
    @PostMapping(value = "/findChangGuanTypeList")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findChangGuanTypeList() {
        LogUtil.d("findChangGuanTypeList","");

        R r = service.findChangGuanTypeList();

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据id删除场馆
     */
    @PostMapping(value = "/deleteChangGuanById")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R deleteChangGuanById(Integer id) {
        LogUtil.d("deleteChangGuanById","id: "+id);

        R r = service.deleteChangGuanById(id);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }


    /**
     * 根据id查询场馆
     */
    @PostMapping(value = "/findChangGuanById")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findChangGuanById(Integer id) {
        LogUtil.d("findChangGuanById","id: "+id);

        R r = service.findChangGuanById(id);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据id修改场馆
     */
    @PostMapping(value = "/updateChangGuan")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R updateChangGuan(Integer id,String name) {
        LogUtil.d("updateChangGuan","id: "+id+" name:"+name);

        R r = service.updateChangGuan(id,name);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据id修改片场
     */
    @PostMapping(value = "/updatePianChang")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R updatePianChang(Integer id,String name) {
        LogUtil.d("updatePianChang","id: "+id+" name:"+name);

        R r = service.updatePianChang(id,name);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据id修改时间段
     */
    @PostMapping(value = "/updatePeriodTime")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R updatePeriodTime(Integer pid,String betTime,Integer sort,Double price,Double weekPrice) {
        LogUtil.d("updatePeriodTime","pid: "+pid+" betTime:"+betTime);

        R r = service.updatePeriodTime(pid,betTime,sort,price,weekPrice);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据场馆ID查询片场列表
     */
    @PostMapping(value = "/findPianChangList")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findPianChangList(Integer id) {
        LogUtil.d("findPianChangList","id: "+id);

        R r = service.findPianChangList(id);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 批量关闭片场
     */
    @PostMapping(value = "/addCloseDateTimeByMore")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R addCloseDateTimeByMore(String times,String ids,String dates) {
        LogUtil.d("addCloseDateTimeByMore","times: "+times+" ids:"+ids+" dates:"+dates);

        R r = service.addCloseDateTimeByMore(times,ids,dates);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 查询订单列表
     */
    @PostMapping(value = "/findOrderList")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findOrderList(String phone,String name,String date,String time,String changdi,String state,Integer page) {
        LogUtil.d("findOrderList","date: "+date+"time: "+time+"changdi: "+changdi+"state: "+state+"page: "+page);

        R r = service.findOrderList(phone,name,date,time,changdi,state,page);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据手机号查询订单列表
     */
    @PostMapping(value = "/findOrderListByPhone")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findOrderListByPhone(String phone) {
        LogUtil.d("findOrderListByPhone","phone: "+phone);

        R r = service.findOrderListByPhone(phone);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据联系人查询订单列表
     */
    @PostMapping(value = "/findOrderListByName")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findOrderListByName(String name) {
        LogUtil.d("findOrderListByName","name: "+name);

        R r = service.findOrderListByName(name);

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

        R r = service.deleteOrderById(id);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 查询场馆列表
     */
    @PostMapping(value = "/findChangGuanList")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findChangGuanList() {
        LogUtil.d("findChangGuanList", "");

        R r = service.findChangGuanList();

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 批量添加片场
     */
    @PostMapping(value = "/addPianChangByMore")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R addPianChangByMore(String id,String name,String num) {
        LogUtil.d("addPianChangByMore", "id:"+id+" name:"+name+" num:"+num);

        R r = service.addPianChangByMore(new Integer(id),name,new Integer(num));

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 批量删除片场
     */
    @PostMapping(value = "/deletePianChangByMore")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R deletePianChangByMore(String ids) {
        LogUtil.d("deletePianChangByMore", "ids:"+ids);

        R r = service.deletePianChangByMore(ids);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 批量删除时间段
     */
    @PostMapping(value = "/deletePeriodTimeByMore")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R deletePeriodTimeByMore(String ids) {
        LogUtil.d("deletePeriodTimeByMore", "ids:"+ids);

        R r = service.deletePeriodTimeByMore(ids);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 批量添加时间段
     */
    @PostMapping(value = "/addPeriodTimeByMore")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R addPeriodTimeByMore(Integer id,String content,Double price,Double weekPrice,Integer sort) {
        LogUtil.d("addPeriodTimeByMore", "id:"+id+" content:"+content+" price:"+price+" weekPrice:"+weekPrice+" sort:"+sort);

        R r = service.addPeriodTimeByMore(id,content,price,weekPrice,sort);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 查询订单列表
     */
    @PostMapping(value = "/findOrderListBySuccess")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findOrderListBySuccess(String start_date,String end_date,String changdi,Integer state,Integer page) {
        LogUtil.d("findOrderList","start_date: "+start_date+"end_date: "+end_date+"changdi: "+changdi+"page: "+page);

        R r = service.findOrderListBySuccess2(start_date,end_date,changdi,state,page);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 导出报表
     */
    @PostMapping(value = "/daochuExcel2")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R daochuExcel2(String start_date, String end_date, String search_changdi,Integer state,HttpSession session) {
        LogUtil.d("findOrderList","start_date: "+start_date+"end_date: "+end_date);
        R r = service.daochuExcel2(start_date,end_date,search_changdi,state,session);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 创建订单
     */
    @PostMapping(value = "/createOrder")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R createOrder(String name, String phone, String pids,String reserveTime,Integer vid,Integer type,HttpSession session) {
        LogUtil.d("createOrderForBack","pids:"+pids+" vid:"+vid);
        R r = service.createOrder(name,phone,pids,reserveTime,vid,type);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 检查未到场，并修改状态为预约失败
     */
    @PostMapping(value = "/refreshOrder")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R refreshOrder() {
        R r = service.refreshOrder();
        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 查询订单列表
     */
    @PostMapping(value = "/findOrderListHistory")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findOrderListHistory(String phone,String name,String date,String time,String changdi,String state,Integer page) {
        LogUtil.d("findOrderListHistory","date: "+date+"time: "+time+"changdi: "+changdi+"state: "+state+"page: "+page);

        R r = service.findOrderListHistory(phone,name,date,time,changdi,state,page);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据手机号查询订单列表
     */
    @PostMapping(value = "/findOrderListHistoryByPhone")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findOrderListHistoryByPhone(String phone) {
        LogUtil.d("findOrderListHistoryByPhone","phone: "+phone);

        R r = service.findOrderListHistoryByPhone(phone);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据联系人查询订单列表
     */
    @PostMapping(value = "/findOrderListHistoryByName")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R findOrderListHistoryByName(String name) {
        LogUtil.d("findOrderListHistoryByName","name: "+name);

        R r = service.findOrderListHistoryByName(name);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 根据id删除订单历史记录
     */
    @PostMapping(value = "/deleteOrderHistoryById")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R deleteOrderHistoryById(Integer id) {
        LogUtil.d("deleteOrderHistoryById","id: "+id);

        R r = service.deleteOrderHistoryById(id);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }

    /**
     * 导出订单历史记录报表
     */
    @PostMapping(value = "/exportOrderListHistory")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public R exportOrderListHistory(String start_date, String end_date, String search_changdi,Integer state,HttpSession session) {
        LogUtil.d("exportOrderListHistory","start_date: "+start_date+"end_date: "+end_date);
        R r = service.exportOrderListHistory(start_date,end_date,search_changdi,state,session);

        //   logService.insertLog(LogActions.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));
        r.ToString();
        return r;
    }


}
