package com.atxca.mybatis.dao;


import com.atxca.data.PO.TypePO;
import com.atxca.mybatis.entity.Order;
import com.atxca.mybatis.entity.PeriodTime;
import com.atxca.mybatis.entity.PeriodTimeClose;
import com.atxca.mybatis.entity.PianChangPO;
import com.atxca.order.PO.OrderPO;
import com.atxca.sportshall.PO.VenuePO;
import com.atxca.sportshall.PO.Vhouse;

import java.util.Date;
import java.util.List;

public interface OrderDao2 extends BaseDao<PeriodTime> {

    List<Order> findOrderListByWaitAndOpenId(int type, String openid);

    List<Order> findOrderListByOpenId(String openid);

    List<PianChangPO> findVenueListByVhouseId2(Integer id);


    int findOrderCountByChangGuanIdAndDateAndStateMore(String date, int type,int id);

    int findPeriodTimeCloseByVidAndDate(String date, Integer id);

    List<Order> findOrderListByReserveTimeLessThan(Date date);

    void updateOrderType(int oid, int type);

    Order findOrderHistoryByOrderId(String orderId);

    void addOrderHistory(Order order);

    void deleteOrderByOid(int oid);

    List<Order> findOrderListByAll();
}
