package com.atxca.order.Controller;

import com.atxca.Util.*;
import com.atxca.Util.PO.Result;
import com.atxca.core.page.Paging;
import com.atxca.order.PO.OrderHistoryPO;
import com.atxca.order.PO.OrderPO;
import com.atxca.order.VO.OrderVO;

import com.atxca.order.service.OrderJpaService;
import com.atxca.sportshall.PO.PeriodTimePO;
import com.atxca.sportshall.PO.VenuePO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 王志鹏
 * @title: OrderController
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 16:19
 */
@Api(tags = "订单API")
@RestController
@RequestMapping(value = "/order")
public class OrderController {


    @Resource
    private OrderJpaService orderService;



    @ApiOperation(value = "查询指定时间场地时段状态", notes = "无")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/qyerListoerxer", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderPO> qyerListoerxer(OrderPO orderPO) {
        Result<OrderPO> result = orderService.qyerListoerxer(orderPO);
        return result;
    }

    @ApiOperation(value = "查询订单编号", notes = "无")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/queryOrderId", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderPO> queryOrderId(OrderPO orderPO) {
        Result<OrderPO> result = orderService.queryOrderId(orderPO);
        return result;
    }


    @ApiOperation(value = "根据用户openid查询用户所有订单", notes = "无")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "openid", required = true),
            @ApiImplicitParam(name = "type", value = "订单状态0为查询全部:1取消,2待审批,3待付款,4预约成功", required = true)
    })
    @RequestMapping(value = "/queryOrderVOforWX", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<List<OrderVO>> queryOrderVOforWX(@RequestParam String openid, @RequestParam int type) {
        Result<List<OrderVO>> result = orderService.queryOrderVOforWX(openid, type);
        return result;

    }

    @ApiOperation(value = "查询订单", notes = "查询预约订单,支持以下条件:场地详细主键,订单号,预约日期,名称,电话,时间段,类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "容量", required = true),
            @ApiImplicitParam(name = "pid", value = "场地详细主键", required = false),
            @ApiImplicitParam(name = "orderId", value = "订单号", required = false),
            @ApiImplicitParam(name = "name", value = "联系人姓名", required = false),
            @ApiImplicitParam(name = "phone", value = "联系人电话", required = false),
            @ApiImplicitParam(name = "betTime", value = "预约时间段", required = false),
            @ApiImplicitParam(name = "reserveTime", value = "预约日期(天)", required = false),
            @ApiImplicitParam(name = "openid", value = "openid", required = false),
            @ApiImplicitParam(name = "type", value = "订单状态", required = false)
    })
    @RequestMapping(value = "/pagequeryOrderPO", method = RequestMethod.POST)
    public Result<List<OrderPO>> pagequeryOrderPO(Paging page, OrderPO orderPO) {
        LogUtil.d("pagequeryOrderPO","");
        Result<List<OrderPO>>  result = orderService.pagequeryOrderPO(page, orderPO);
        return result;
    }

    @ApiOperation(value = "创建预约", notes = "拦截 订单状态为待审批,和已预约的订单 ,参数参考OrderPO 注意此接口参数为集合类型")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> createOrder(@RequestBody List<OrderPO> orderPOS) throws SCException {
        Result<Boolean>  result = orderService.createOrder(orderPOS);
        return result;
    }

    @RequestMapping(value = "/createOrderForBack", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> createOrderForBack(OrderPO orderPO,String pids) throws SCException {
        Result<Boolean>  result = orderService.createOrderForBack(orderPO,pids);
        return result;
    }

    @ApiOperation(value = "审批通过", notes = "修改状态为待付款")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/approvedPass", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> approvedPass(@RequestParam String orderId) {
        Result<Boolean>  result = orderService.approvedPass(orderId);
        return result;
    }

    @ApiOperation(value = "审批拒绝", notes = "修改状态为取消")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/approvedFail", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> approvedFail(@RequestParam String orderId) {
        Result<Boolean>  result = orderService.approvedFail(orderId);
        return result;
    }


    @ApiOperation(value = "付款确认", notes = "修改订单为预约成功")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/payCheck", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> payCheck(@RequestParam String orderId) {
        Result<Boolean>  result = orderService.payCheck(orderId);
        return result;
    }

    @ApiOperation(value = "取消订单(付款)", notes = "修改订单为取消")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/payCancel", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> payCancel(@RequestParam String orderId) {
        Result<Boolean>  result = orderService.payCancel(orderId);
        return result;
    }

    @ApiOperation(value = "修改订单状态", notes = "修改订单,暂不能用!!!")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateOrder(OrderPO orderPO) {
        Result<Boolean>  result = orderService.updateOrder(orderPO);
        return result;
    }


    @ApiOperation(value = "查询指定日期场地剩余", notes = "无")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "times", value = "日期", required = true),
            @ApiImplicitParam(name = "vid", value = "场地主键", required = true)
    })
    @RequestMapping(value = "/querDateNum", method = RequestMethod.POST)
    public Result<List<PeriodTimePO>> querDateNum(@RequestParam(value = "times") String times, @RequestParam Integer vid) {
        Result<List<PeriodTimePO>>  result = orderService.querDateNum(times, vid);
        return result;
    }

    @ApiOperation(value = "查询指定日期场馆下所有场地剩余", notes = "无")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "times", value = "日期2019-05-08", required = true),
            @ApiImplicitParam(name = "type", value = "场馆主键", required = true)
    })
    @RequestMapping(value = "/getVenueSizefMonth", method = RequestMethod.POST)
    public Result<List<VenuePO>> getVenueSizefMonth(@RequestParam String times, @RequestParam int type) {
//        Date start = new Date();
//        String YYYY_MM_HH = DateUtil.addDate(new Date(), 30);
//        Date end = DateUtil.parseDate(YYYY_MM_HH + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
//        List<String> stringList = DateUtil.getBetweenDays(start, end);
        Result<List<VenuePO>>  result = orderService.getVenueSizefMonth(times,type);
        return result;
    }


    @ApiOperation(value = "查询订单历史记录", notes = "查询预约订单,支持以下条件:场地详细主键,订单号,预约日期,名称,电话,时间段,类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "容量", required = true),
            @ApiImplicitParam(name = "pid", value = "场地详细主键", required = false),
            @ApiImplicitParam(name = "orderId", value = "订单号", required = false),
            @ApiImplicitParam(name = "name", value = "联系人姓名", required = false),
            @ApiImplicitParam(name = "phone", value = "联系人电话", required = false),
            @ApiImplicitParam(name = "betTime", value = "预约时间段", required = false),
            @ApiImplicitParam(name = "reserveTime", value = "预约日期(天)", required = false),
            @ApiImplicitParam(name = "openid", value = "openid", required = false),
            @ApiImplicitParam(name = "type", value = "订单状态", required = false)
    })
    @RequestMapping(value = "/pagequeryOrderHistory", method = RequestMethod.POST)
    public Result<List<OrderHistoryPO>> pagequeryOrderHistory(Paging page, OrderHistoryPO orderPO) {
        LogUtil.d("pagequeryOrderPO","");
        Result<List<OrderHistoryPO>> result = orderService.pagequeryOrderHistory(page, orderPO);
        return result;
    }



}