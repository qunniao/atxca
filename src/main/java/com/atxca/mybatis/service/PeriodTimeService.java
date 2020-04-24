package com.atxca.mybatis.service;


import com.atxca.Util.R;

import javax.servlet.http.HttpSession;

public interface PeriodTimeService {

    R findPeriodTimeListById(Integer id);

    R findChangDiList(String date, String time, Integer changguan_type);

    R updatePianChangState(String date, String time, Integer id, Integer old_state, Integer new_state);

    R findChangGuanTypeList();

    R deleteChangGuanById(Integer id);

    R findChangGuanById(Integer id);

    R findPianChangList(Integer id);

    R updateChangGuan(Integer id, String name);

    R updatePianChang(Integer id, String name);

    R updatePeriodTime(Integer pid, String betTime, Integer sort, Double price, Double weekPrice);

    R addCloseDateTimeByMore(String times,String ids, String dates);

    R findOrderList(String phone, String name, String date, String time, String changdi, String state, Integer page);

    R findChangGuanList();

    R deleteOrderById(Integer id);

    R addPianChangByMore(Integer id, String name, Integer num);

    R deletePianChangByMore(String ids);

    R deletePeriodTimeByMore(String ids);

    R addPeriodTimeByMore(Integer id, String content, Double price, Double weekPrice, Integer sort);

    R findOrderListBySuccess(String start_date, String end_date, String changdi,String state, Integer page);

    R daochuExcel2(String start_date, String end_date, String search_changdi, Integer state, HttpSession session);

    R findOrderListByPhone(String phone);

    R findOrderListByName(String name);

    R createOrder(String name, String phone, String pids, String reserveTime, Integer vid, Integer type);

    R refreshOrder();

    R findOrderListBySuccess2(String start_date, String end_date, String changdi, Integer state, Integer page);
}
