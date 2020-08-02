package com.atxca.job;

import com.atxca.mybatis.entity.Order;
import com.atxca.mybatis.entity.PeriodTimeClose;
import com.atxca.mybatis.service.OrderService;

import com.atxca.mybatis.service.PeriodTimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author 黄滚
 * Created by hg at 2020/1/26 7:57 下午
 */
@Component
@Slf4j
public class SyncOrderHistory {



    @Resource
    OrderService orderService;

    @Resource
    PeriodTimeService periodTimeService;

    /**
     * 每12小时执行一次，同步过期订单到订单记录
     */
    @Scheduled(initialDelay=1000*30,fixedDelay = 1000*60*60*12)
    public void run(){
        Date date = new Date();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String d = sf.format(date);
        try {
            date = sf.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.error("当前日期："+d);
        List<Order> list = orderService.findOrderListByReserveTimeLessThan(date);
        log.error("size:"+list.size());
        for (Order order:list) {
            log.error("开始=================");
            //过期订单如果状态为预约成功2，则改成预约未到场5
            if(order.getType()==2){
                order.setType(5);
                orderService.updateOrderType(order.getOid(),order.getType());
                log.error("订单id:{},原状态：2,现状态：{}",order.getOid(),order.getType());
            }
            Order orderHistory = orderService.findOrderHistoryByOrderId(order.getOrderId());
            if(orderHistory!=null){
                log.error("已存在历史订单，订单id:{}",order.getOrderId());
            }else{
                orderService.addOrderHistory(order);
            }
            orderService.deleteOrderByOid(order.getOid());
            log.error("已删除订单:{}",order.getOid());
        }

        List<Order> list2 = orderService.findOrderListByAll();
        log.error("size:"+list2.size());
        for (Order order:list2) {
            //log.error("开始=================");

            Order orderHistory = orderService.findOrderHistoryByOrderId(order.getOrderId());
            if(orderHistory!=null){
                //log.error("已存在历史订单，订单id:{}",order.getOrderId());
                if(orderHistory.getType()!=order.getType()){
                    periodTimeService.deleteOrderHistoryById(orderHistory.getOid());
                    orderService.addOrderHistory(order);
                }
            }else{
                orderService.addOrderHistory(order);
            }
            if(order.getType()==5 || order.getType()==4){
                orderService.deleteOrderByOid(order.getOid());
                log.error("已删除订单:{}",order.getOrderId());
            }

        }

    }

    /**
     * 每24小时执行一次，删除过期内部关闭的场馆设置
     */
    @Scheduled(initialDelay=1000*60,fixedDelay = 1000*60*60*24)
    public void deletePeriodTimeClose(){
        log.error("删除内部关闭开始===========");
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String d = sf.format(date);
        try {
            date = sf.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        log.error("当前日期："+d);
        List<PeriodTimeClose> list = periodTimeService.findPeriodTimeCloseListByCloseDateLessThan(date);
        log.error("size:"+list.size());
        for (PeriodTimeClose item:list) {
            periodTimeService.deletePeriodTimeCloseById(item.getId());
        }
    }
}
