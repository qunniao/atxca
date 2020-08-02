package com.atxca.order.Dao;

import com.atxca.order.PO.OrderHistoryPO;
import com.atxca.order.PO.OrderPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 王志鹏
 * @title: OrderDao
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 15:30
 */
public interface OrderHistoryDao extends JpaRepository<OrderHistoryPO, Integer>, JpaSpecificationExecutor {


}
