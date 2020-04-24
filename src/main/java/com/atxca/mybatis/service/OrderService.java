package com.atxca.mybatis.service;


import com.atxca.Util.R;

import javax.servlet.http.HttpSession;

public interface OrderService {


    R findOrderListByWaitAndOpenId(String openid);

    R findOrderListByOpenId(String openid);

    R findYuYueListByChangGuanId(Integer id);

    R findYuYueDateCount(Integer id);

    R findYuYuePianChangList(Integer id, String date);

    R createOrder(String name, String phone, String pids, String reserveTime, Integer vid, String openid);
}
