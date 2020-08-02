package com.atxca.order.service;

import com.atxca.Util.PO.Result;
import com.atxca.core.page.Paging;
import com.atxca.order.PO.OrderHistoryPO;
import com.atxca.order.PO.OrderPO;
import com.atxca.order.VO.OrderVO;
import com.atxca.sportshall.PO.PeriodTimePO;
import com.atxca.sportshall.PO.VenuePO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderJpaService {
    Result<List<OrderHistoryPO>> pagequeryOrderHistory(Paging page, OrderHistoryPO orderPO);

    Result<List<VenuePO>> getVenueSizefMonth(String times, int type);

    Result<OrderPO> qyerListoerxer(OrderPO orderPO);

    Result<OrderPO> queryOrderId(OrderPO orderPO);

    Result<List<OrderVO>> queryOrderVOforWX(String openid, int type);

    Result<List<OrderPO>> pagequeryOrderPO(Paging page, OrderPO orderPO);

    Result<Boolean> createOrder(List<OrderPO> orderPOS);

    Result<Boolean> createOrderForBack(OrderPO orderPO, String pids);

    Result<Boolean> approvedPass(String orderId);

    Result<Boolean> approvedFail(String orderId);

    Result<Boolean> payCheck(String orderId);

    Result<Boolean> payCancel(String orderId);

    Result<Boolean> updateOrder(OrderPO orderPO);

    Result<List<PeriodTimePO>> querDateNum(String times, Integer vid);
}
