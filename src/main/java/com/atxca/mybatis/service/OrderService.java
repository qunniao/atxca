package com.atxca.mybatis.service;


import com.atxca.Util.R;
import com.atxca.mybatis.entity.Order;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

public interface OrderService {


    R findOrderListByWaitAndOpenId(String openid);

    R findOrderListByOpenId(String openid);

    R findYuYueListByChangGuanId(Integer id);

    R findYuYueDateCount(Integer id);

    R findYuYuePianChangList(Integer id, String date);

    R createOrder(String name, String phone, String pids, String reserveTime, Integer vid, String openid);

    List<Order> findOrderListByReserveTimeLessThan(Date date);

    void updateOrderType(int oid, int type);

    Order findOrderHistoryByOrderId(String orderId);

    void addOrderHistory(Order order);

    void deleteOrderByOid(int oid);

    List<Order> findOrderListByAll();
}
