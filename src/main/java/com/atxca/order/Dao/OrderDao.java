package com.atxca.order.Dao;

import com.atxca.order.PO.OrderPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author 王志鹏
 * @title: OrderDao
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 15:30
 */
public interface OrderDao extends JpaRepository<OrderPO, Integer>, JpaSpecificationExecutor {


    OrderPO queryByOrderId(String orderId);

    int countByTypeNotInAndBetTimeAndReserveTime(List<Integer> type, String betTime, String reserveTime);

    List<OrderPO> queryByType(int Type);

    List<OrderPO> queryByPrice(BigDecimal price);

    List<OrderPO> queryByTypeNotAndAndReserveTime(int type, String reserveTime);

    @Query(value = "SELECT oid ,reserveTime, count(1) AS pid FROM atxca.order WHERE type = 4 and createTime between ?1 and ?2  GROUP BY pid,reserveTime; ", nativeQuery = true)
    List<OrderPO> queryForPicture(String startTime, String endTime);

    List<OrderPO> queryByOpenidAndType(String openid, int type);

    List<OrderPO> queryByOpenid(String openid);

    OrderPO queryByTypeNotAndReserveTimeAndPid(int type, String reserveTime, int vid);

    OrderPO queryByBetTimeAndReserveTimeAndPidAndTypeNot(String betTime, String rtime, int pid, int type);
}
