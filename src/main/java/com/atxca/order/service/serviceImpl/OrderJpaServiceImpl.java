package com.atxca.order.service.serviceImpl;

import com.atxca.Util.*;
import com.atxca.Util.PO.Result;
import com.atxca.core.page.Paging;
import com.atxca.mybatis.dao.ChangGuanPeriodTimeDao;
import com.atxca.order.Dao.OrderDao;
import com.atxca.order.Dao.OrderHistoryDao;
import com.atxca.order.PO.OrderHistoryPO;
import com.atxca.order.PO.OrderPO;
import com.atxca.order.VO.OrderVO;
import com.atxca.order.service.OrderJpaService;
import com.atxca.sportshall.Dao.PeriodTimeDao;
import com.atxca.sportshall.Dao.VenueDao;
import com.atxca.sportshall.Dao.VenueOrderDao;
import com.atxca.sportshall.Dao.VhouseDao;
import com.atxca.sportshall.PO.PeriodTimePO;
import com.atxca.sportshall.PO.VenueOrderPO;
import com.atxca.sportshall.PO.VenuePO;
import com.atxca.sportshall.PO.Vhouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OrderJpaServiceImpl implements OrderJpaService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderHistoryDao orderHistoryDao;

    @Autowired
    private VenueDao venueDao;

    @Autowired
    private VhouseDao vhouseDao;

    @Autowired
    private PeriodTimeDao periodTimeDao;



    @Autowired
    private VenueOrderDao venueOrderDao;

    @Resource
    private ChangGuanPeriodTimeDao frontDao;


    public static final int ORDER_TYPE_WAIT = 1;//预约待审核
    public static final int ORDER_TYPE_SUCCESS = 2;//预约成功
    public static final int ORDER_TYPE_REACH = 7;//预约到场
    public static final int ORDER_TYPE_REACH_ALIPAY = 6;//支付宝到场
    public static final int ORDER_TYPE_FAIL = 4;//预约失败
    public static final int ORDER_TYPE_NOT_REACH = 5;//预约未到

    @Override
    public Result<List<OrderHistoryPO>> pagequeryOrderHistory(Paging page, OrderHistoryPO orderPO) {
        Pageable pageable = PageRequest.of(page.getPageNum() - 1, page.getPageSize(), Sort.Direction.DESC, "oid");

        Specification querySpecifi = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (GeneralUtils.notZero(orderPO.getPid())) {
                    predicates.add(criteriaBuilder.equal(root.get("pid"), orderPO.getPid()));
                }

                if (GeneralUtils.notEmpty(orderPO.getOrderId())) {
                    predicates.add(criteriaBuilder.equal(root.get("orderId"), orderPO.getOrderId()));
                }

                if (GeneralUtils.notEmpty(orderPO.getReserveTime())) {
                    predicates.add(criteriaBuilder.equal(root.get("reserveTime"), orderPO.getReserveTime()));
                }

                if (GeneralUtils.notEmpty(orderPO.getName())) {
                    predicates.add(criteriaBuilder.equal(root.get("name"), orderPO.getName()));
                }

                if (GeneralUtils.notEmpty(orderPO.getPhone())) {
                    predicates.add(criteriaBuilder.equal(root.get("phone"), orderPO.getPhone()));
                }

                if (GeneralUtils.notEmpty(orderPO.getBetTime())) {
                    predicates.add(criteriaBuilder.equal(root.get("betTime"), orderPO.getBetTime()));
                }

                if (GeneralUtils.notZero(orderPO.getType())) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), orderPO.getType()));
                }

                if (GeneralUtils.notEmpty(orderPO.getOpenid())) {
                    predicates.add(criteriaBuilder.equal(root.get("openid"), orderPO.getOpenid()));
                }

                if (GeneralUtils.notZero(orderPO.getVid())) {
                    predicates.add(criteriaBuilder.equal(root.get("vid"), orderPO.getVid()));
                }

                criteriaQuery.groupBy(root.get("oid"));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return ResultUtil.success(orderHistoryDao.findAll(querySpecifi, pageable));
    }

    @Override
    public Result<List<VenuePO>> getVenueSizefMonth(String times, int type) {
        List<VenuePO> venuePOS = venueDao.queryByType(type);
        List<OrderPO> poList = orderDao.queryByTypeNotAndAndReserveTime(4, times);

        for (VenuePO venue : venuePOS) {
            List<PeriodTimePO> timePOS = new ArrayList<>();

            List<String> betTimeList = new ArrayList<>();

            for (OrderPO orderPO : poList) {
                betTimeList.add(orderPO.getBetTime());
            }

            for (PeriodTimePO timePO : venue.getPeriodTimePOList()) {
                if (betTimeList.contains(timePO.getBetTime())) {
                    continue;
                }
                timePOS.add(timePO);
            }
            venue.setPeriodTimePOList(timePOS);

        }

        return ResultUtil.success(venuePOS);
    }

    @Override
    public Result<OrderPO> qyerListoerxer(OrderPO orderPO) {
        OrderPO lisit = orderDao.queryByTypeNotAndReserveTimeAndPid(4, orderPO.getReserveTime(), orderPO.getPid());
        return ResultUtil.success(lisit);
    }

    @Override
    public Result<OrderPO> queryOrderId(OrderPO orderPO) {
        String betTime = orderPO.getBetTime();
        String reserveTime = orderPO.getReserveTime();
        int pid = orderPO.getPid();
        int type = ORDER_TYPE_FAIL;

        OrderPO orderPO2 = orderDao.queryByBetTimeAndReserveTimeAndPidAndTypeNot(betTime, reserveTime, pid, type);


        return ResultUtil.success(orderPO2);
    }

    @Override
    public Result<List<OrderVO>> queryOrderVOforWX(String openid, int type) {
        List<OrderPO> poList = new ArrayList<>();
        if (type == 0) {
            poList = orderDao.queryByOpenid(openid);
        } else {
            poList = orderDao.queryByOpenidAndType(openid, type);
        }

        List<OrderVO> voList = new ArrayList<>();

        for (OrderPO orderPO : poList) {

            PeriodTimePO periodTimePO = periodTimeDao.getOne(orderPO.getPid());
            VenuePO venuePO =venueDao.getOne(orderPO.getVid());
            Vhouse vhouse = vhouseDao.getOne(venuePO.getType());

            OrderVO orderVO = new OrderVO();

            orderVO.setName(vhouse.getName());
            orderVO.setVName(venuePO.getVName());
            orderVO.setReserveTime(orderPO.getReserveTime());
            orderVO.setBetTime(periodTimePO.getBetTime());
            orderVO.setUserName(orderPO.getName());
            orderVO.setUserPhone(orderPO.getPhone());
            orderVO.setOrderId(orderPO.getOrderId());
            orderVO.setType(orderPO.getType());
            voList.add(orderVO);
        }

        return ResultUtil.success(voList);
    }

    @Override
    public Result<List<OrderPO>> pagequeryOrderPO(Paging page, OrderPO orderPO) {
        Pageable pageable = PageRequest.of(page.getPageNum() - 1, page.getPageSize(), Sort.Direction.DESC, "oid");

        Specification querySpecifi = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                if (GeneralUtils.notZero(orderPO.getPid())) {
                    predicates.add(criteriaBuilder.equal(root.get("pid"), orderPO.getPid()));
                }

                if (GeneralUtils.notEmpty(orderPO.getOrderId())) {
                    predicates.add(criteriaBuilder.equal(root.get("orderId"), orderPO.getOrderId()));
                }

                if (GeneralUtils.notEmpty(orderPO.getReserveTime())) {
                    predicates.add(criteriaBuilder.equal(root.get("reserveTime"), orderPO.getReserveTime()));
                }

                if (GeneralUtils.notEmpty(orderPO.getName())) {
                    predicates.add(criteriaBuilder.equal(root.get("name"), orderPO.getName()));
                }

                if (GeneralUtils.notEmpty(orderPO.getPhone())) {
                    predicates.add(criteriaBuilder.equal(root.get("phone"), orderPO.getPhone()));
                }

                if (GeneralUtils.notEmpty(orderPO.getBetTime())) {
                    predicates.add(criteriaBuilder.equal(root.get("betTime"), orderPO.getBetTime()));
                }

                if (GeneralUtils.notZero(orderPO.getType())) {
                    predicates.add(criteriaBuilder.equal(root.get("type"), orderPO.getType()));
                }

                if (GeneralUtils.notEmpty(orderPO.getOpenid())) {
                    predicates.add(criteriaBuilder.equal(root.get("openid"), orderPO.getOpenid()));
                }

                if (GeneralUtils.notZero(orderPO.getVid())) {
                    predicates.add(criteriaBuilder.equal(root.get("vid"), orderPO.getVid()));
                }

                criteriaQuery.groupBy(root.get("oid"));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return ResultUtil.success(orderDao.findAll(querySpecifi, pageable));
    }

    @Override
    public Result<Boolean> createOrder(List<OrderPO> orderPOS) {
        for (OrderPO orderPO : orderPOS) {

            if (queryOrderType(orderPO.getBetTime(), orderPO.getReserveTime())) {
                throw new SCException(11111, "预约失败,该时段被他人预约,请刷新!");
            }

            OrderPO orderPO1 = new OrderPO();
            orderPO1.setType(ORDER_TYPE_WAIT);
            orderPO1.setOrderId("DL" + RandomUtil.getRandomStringByLength(4) + DateUtil.current());

            PeriodTimePO periodTimePO2 = periodTimeDao.findById(orderPO.getPid()).get();
            // int vid = periodTimePO2.getVid();
            VenueOrderPO venueOrderPO = venueOrderDao.queryByReserveTimeAndAndPid(orderPO.getReserveTime(), periodTimePO2.getPid());

            if (venueOrderPO != null) {
                if(venueOrderPO.getType()!=1){
                    throw new SCException(11111, "场地状态不为空置可被预约!不能预约!请联系工作人员!");
                }
            }

            // VenuePO venuePO = venueDao.getOne(periodTimePO2.getVid());
/*
            if(venuePO.getStatus()==0){
                throw new SCException(11111, "场地状态为禁用,不能预约!");
            }
*/
            if (GeneralUtils.notEmpty(orderPO.getOpenid())) {
                orderPO1.setOpenid(orderPO.getOpenid());
            }
            //  orderPO1.setVid(vid);
            orderPO1.setPid(orderPO.getPid());
            orderPO1.setPrice(orderPO.getPrice());
            orderPO1.setName(orderPO.getName());
            orderPO1.setPhone(orderPO.getPhone());
            orderPO1.setBetTime(periodTimePO2.getBetTime());//预约时间段
            orderPO1.setReserveTime(orderPO.getReserveTime());//指定日期
            orderPO1.setCreateTime(DateUtil.getDateToTimestamp(new Date()));//创建时间
            orderPO1.setRemakes(orderPO.getRemakes());
            orderDao.save(orderPO1);
        }


        return ResultUtil.success(true);
    }

    @Override
    public Result<Boolean> createOrderForBack(OrderPO orderPO, String pids) {
        LogUtil.d("createOrderForBack","pids:"+pids);
        LogUtil.d("createOrderForBack","orderPO.getReserveTime():"+orderPO.getReserveTime());
        LogUtil.d("createOrderForBack","vid:"+orderPO.getVid()+" type:"+orderPO.getType());
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sf.parse(orderPO.getReserveTime().trim());//
        }catch (Exception e){

        }
        int day=1;
        if (d != null) {
            day = d.getDay();
            LogUtil.d("createOrderForBack","day:"+day);
        }

        String text="";
        switch (day) {

            case 1:
                text = "星期一";
                break;
            case 2:
                text = "星期二";
                break;
            case 3:
                text = "星期三";
                break;
            case 4:
                text = "星期四";
                break;
            case 5:
                text = "星期五";
                break;
            case 6:
                text = "星期六";
                break;
            case 0:
                text = "星期日";
                break;
        }
        LogUtil.d("createOrderForBack","星期几："+text);
        String[] p = pids.split(",");
        for(int i=0;i<p.length;i++){
            if("".equals(p[i]))continue;


            orderPO.setPid(new Integer(p[i]));
            OrderPO orderPO2 = orderDao.queryByTypeNotAndReserveTimeAndPid(4, orderPO.getReserveTime(), orderPO.getPid());
            if (orderPO2 != null) {
                throw new SCException(11111, "预约失败,该时段被他人预约,请刷新!");
            }

            OrderPO orderPO1 = new OrderPO();
            orderPO1.setType(orderPO.getType());
            orderPO1.setOrderId("DL" + RandomUtil.getRandomStringByLength(4) + DateUtil.current());

            PeriodTimePO periodTimePO2 = periodTimeDao.findById(orderPO.getPid()).get();


            if (GeneralUtils.notEmpty(orderPO.getOpenid())) {
                orderPO1.setOpenid(orderPO.getOpenid());
            }
            orderPO1.setPid(orderPO.getPid());
            orderPO1.setVid(orderPO.getVid());
            orderPO1.setName(orderPO.getName());
            orderPO1.setPhone(orderPO.getPhone());
            orderPO1.setBetTime(periodTimePO2.getBetTime());//预约时间段
            orderPO1.setReserveTime(orderPO.getReserveTime());//指定日期
            orderPO1.setCreateTime(DateUtil.getDateToTimestamp(new Date()));//创建时间
            orderPO1.setRemakes(orderPO.getRemakes());

            switch (day) {

                case 1:

                case 2:

                case 3:

                case 4:

                case 5:
                    orderPO1.setPrice(periodTimePO2.getPrice());
                    break;
                case 6:

                case 0:
                    orderPO1.setPrice(periodTimePO2.getWeekPrice());
                    break;
            }

            orderDao.save(orderPO1);


        }



        return ResultUtil.success(true);
    }

    @Override
    public Result<Boolean> approvedPass(String orderId) {
        OrderPO orderPO = orderDao.queryByOrderId(orderId);
        orderPO.setType(ORDER_TYPE_SUCCESS);//等待付款

        orderDao.save(orderPO);

        return ResultUtil.success(true);
    }

    @Override
    public Result<Boolean> approvedFail(String orderId) {
        OrderPO orderPO = orderDao.queryByOrderId(orderId);
        orderPO.setType(ORDER_TYPE_FAIL);//取消

        orderDao.save(orderPO);
        return ResultUtil.success(true);
    }

    @Override
    public Result<Boolean> payCheck(String orderId) {
        OrderPO orderPO = orderDao.queryByOrderId(orderId);
        orderPO.setType(ORDER_TYPE_REACH);//预约成功

        orderDao.save(orderPO);
        return ResultUtil.success(true);
    }

    @Override
    public Result<Boolean> payCancel(String orderId) {
        OrderPO orderPO = orderDao.queryByOrderId(orderId);
        orderPO.setType(ORDER_TYPE_FAIL);//取消

        orderDao.save(orderPO);
        return ResultUtil.success(true);
    }

    @Override
    public Result<Boolean> updateOrder(OrderPO orderPO) {
        LogUtil.d("updateOrder","type:"+orderPO.getType()+" oid:"+orderPO.getOrderId());

        OrderPO orderPO1 = orderDao.queryByOrderId(orderPO.getOrderId());
        orderPO1.setType(orderPO.getType());
        orderPO1.setName(orderPO.getName());
        orderPO1.setPhone(orderPO.getPhone());
        orderPO1.setPrice(orderPO.getPrice());
        orderDao.save(orderPO1);
        return ResultUtil.success(true);
    }

    @Override
    public Result<List<PeriodTimePO>> querDateNum(String times, Integer vid) {
        List<OrderPO> poList = orderDao.queryByTypeNotAndAndReserveTime(4, times);
        // List<PeriodTimePO> timePOList = periodTimeDao.queryByVid(vid);
        List<PeriodTimePO> timePOS = new ArrayList<>();
        List<String> betTimeList = new ArrayList<>();

        for (OrderPO orderPO : poList) {
            betTimeList.add(orderPO.getBetTime());
        }
/*
        for (PeriodTimePO timePO : timePOList) {
            if (betTimeList.contains(timePO.getBetTime())) {
                continue;
            }
            timePOS.add(timePO);
        }
*/
        return ResultUtil.success(timePOS);
    }

    private boolean queryOrderType(String betTime, String reserveTime) {
        List<Integer> notType = new ArrayList<>();
        notType.add(ORDER_TYPE_FAIL);

        int aa = orderDao.countByTypeNotInAndBetTimeAndReserveTime(notType, betTime, reserveTime);

        if (aa == 0) {
            return false;
        } else {
            return true;
        }

    }

    public boolean juidy(String betTime, String reserveTime) {

        String[] strArray = betTime.split("-");
        String[] strA = strArray[0].split(":");
        String[] strB = strArray[1].split(":");

        int start = Integer.parseInt(strA[0]);
        int end = Integer.parseInt(strB[0]);

        Date theDate = DateUtil.parseDate(reserveTime + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        String week = DateUtil.getWeekOfDate(theDate);
        int cha = end - start;
        if (week.equals("星期六") || week.equals("星期日")) {
            if (cha != 2) {
                return true;
            }
        } else {
            if (cha != 1) {
                return true;
            }
        }
        return false;
    }

}
