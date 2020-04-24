package com.atxca.mybatis.dao;


import com.atxca.data.PO.TypePO;
import com.atxca.mybatis.entity.Order;
import com.atxca.mybatis.entity.PeriodTime;
import com.atxca.mybatis.entity.PeriodTimeClose;
import com.atxca.order.PO.OrderPO;
import com.atxca.sportshall.PO.VenuePO;
import com.atxca.sportshall.PO.Vhouse;

import java.util.List;

public interface ChangGuanPeriodTimeDao extends BaseDao<PeriodTime> {
    List<PeriodTime> findPeriodTimeListById(Integer id);

    List<Vhouse> findVhouseListByAll();

    List<VenuePO> findVenueListByVhouseId(int id);

    List<OrderPO> findOrderListByDateTime(String reserveTime, String betTime);

    List<PeriodTimeClose> findPeriodTimeCloseListByVid(int venues_id);

    List<PeriodTime> findPeriodTimeListByVidAndTime(int vhouse_id, String betTime);

    void addPeriodTimeClose(PeriodTimeClose periodTimeClose);

    void updateOrderState(OrderPO orderPO);

    List<PeriodTimeClose> findPeriodTimeCloseListByVidAndDateTime(Integer venues_id, String close_date, String close_time);

    void deletePeriodTimeCloseByVidAndDateTime(Integer venues_id, String close_date, String close_time);

    Vhouse findVhouseByVnenueId(Integer id);

    void saveOrder(Order orderPO1);

    List<OrderPO> findOrderListByDateTimeAndVid(String reserveTime, String betTime, Integer vid);

    List<TypePO> findChangGuanTypeList();

    List<Vhouse> findVhouseListByType(Integer type);

    void deleteChangGuanById(Integer id);

    Vhouse findChangGuanById(Integer id);

    void updateChangGuan(Vhouse item);

    VenuePO findVenueById(Integer id);

    void updatePianChang(VenuePO item);

    PeriodTime findPeriodTimeByPid(Integer pid);

    void updatePeriodTime(PeriodTime item);

    List<Order> findOrderListByAll(int page, int pageSize, String phone, String name);

    List<Order> findOrderListByChangGuan(String changdi, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByAllAndState(Integer type, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByChangGuanAndState(Integer type, String changdi, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByAllAndDateTime(String reserveTime, String betTime, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByAllAndDate(String reserveTime, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByAllAndTime(String betTime, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByAllAndStateAndDateTime(String type, String reserveTime, String betTime, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByAllAndStateAndDate(String type, String reserveTime, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByAllAndStateAndTime(String type, String betTime, int page, int pageSize, String phone, String name);
    

    List<Order> findOrderListByChangGuanAndDateTime(String changdi, String reserveTime, String betTime, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByChangGuanAndDate(String changdi, String reserveTime, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByChangGuanAndTime(String changdi, String betTime, int page, int pageSize, String phone, String name);


    List<Order> findOrderListByChangGuanAndStateAndDateTime(String type, String changdi, String reserveTime, String betTime, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByChangGuanAndStateAndDate(String type, String changdi, String reserveTime, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByChangGuanAndStateAndTime(String type, String changdi, String betTime, int page, int pageSize, String phone, String name);

    void deleteOrderById(Integer id);

    void addPianChang(VenuePO item);

    void deletePianChangById(Integer id);

    void deletePeriodTimeById(Integer id);

    void addPeriodTime(PeriodTime item);

    List<Order> findOrderListByAllAndStateStartDateEndDate(Integer type,String start_date, String end_date, int page, int pageSize);

    List<Order> findOrderListByChangGuanAndStateStartDateEndDate(Integer type, String changdi, String start_date, String end_date, int page, int pageSize);

    List<Order> findOrderListByPhone(String phone);

    List<Order> findOrderListByName(String name);

    void addOrder(Order order);

    void updateOrderState2(Order order);

    List<Order> findOrderListByAllAndStateSuccess(int orderTypeReach, int orderTypeReachAlipay, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByAllAndStateStartDateEndDateSuccess(int orderTypeReach, int orderTypeReachAlipay, String start_date, String end_date, int page, int pageSize);

    List<Order> findOrderListByChangGuanAndStateSuccess(int orderTypeReach, int orderTypeReachAlipay, String changdi, int page, int pageSize, String phone, String name);

    List<Order> findOrderListByChangGuanAndStateStartDateEndDateSuccess(int orderTypeReach, int orderTypeReachAlipay, String changdi, String start_date, String end_date, int page, int pageSize);
}
