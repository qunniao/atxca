package com.atxca.order.Controller;

import com.atxca.Util.*;
import com.atxca.Util.PO.Result;
import com.atxca.core.page.Paging;
import com.atxca.mybatis.dao.ChangGuanPeriodTimeDao;
import com.atxca.order.Dao.OrderDao;
import com.atxca.order.PO.OrderPO;
import com.atxca.order.VO.OrderVO;
import com.atxca.sportshall.Dao.PeriodTimeDao;
import com.atxca.sportshall.Dao.VenueDao;
import com.atxca.sportshall.Dao.VenueOrderDao;
import com.atxca.sportshall.Dao.VhouseDao;
import com.atxca.sportshall.PO.PeriodTimePO;
import com.atxca.sportshall.PO.VenueOrderPO;
import com.atxca.sportshall.PO.VenuePO;
import com.atxca.sportshall.PO.Vhouse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 王志鹏
 * @title: OrderController
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 16:19
 */
@Api(tags = "订单API")
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PeriodTimeDao periodTimeDao;

    @Autowired
    private VenueDao venueDao;

    @Autowired
    private VhouseDao vhouseDao;

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


    @ApiOperation(value = "查询指定时间场地时段状态", notes = "无")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/qyerListoerxer", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderPO> qyerListoerxer(OrderPO orderPO) {
        OrderPO lisit = orderDao.queryByTypeNotAndReserveTimeAndPid(4, orderPO.getReserveTime(), orderPO.getPid());
        return ResultUtil.success(lisit);
    }

    @ApiOperation(value = "查询订单编号", notes = "无")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/queryOrderId", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<OrderPO> queryOrderId(OrderPO orderPO) {
        String betTime = orderPO.getBetTime();
        String reserveTime = orderPO.getReserveTime();
        int pid = orderPO.getPid();
        int type = ORDER_TYPE_FAIL;

        OrderPO orderPO2 = orderDao.queryByBetTimeAndReserveTimeAndPidAndTypeNot(betTime, reserveTime, pid, type);


        return ResultUtil.success(orderPO2);
    }


    @ApiOperation(value = "根据用户openid查询用户所有订单", notes = "无")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openid", value = "openid", required = true),
            @ApiImplicitParam(name = "type", value = "订单状态0为查询全部:1取消,2待审批,3待付款,4预约成功", required = true)
    })
    @RequestMapping(value = "/queryOrderVOforWX", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<List<OrderVO>> queryOrderVOforWX(@RequestParam String openid, @RequestParam int type) {
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

    @ApiOperation(value = "查询订单", notes = "查询预约订单,支持以下条件:场地详细主键,订单号,预约日期,名称,电话,时间段,类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "当前页", required = true),
            @ApiImplicitParam(name = "pageSize", value = "容量", required = true),
            @ApiImplicitParam(name = "pid", value = "场地详细主键", required = false),
            @ApiImplicitParam(name = "orderId", value = "订单号", required = false),
            @ApiImplicitParam(name = "name", value = "联系人姓名", required = false),
            @ApiImplicitParam(name = "phone", value = "联系人电话", required = false),
            @ApiImplicitParam(name = "betTime", value = "预约时间段", required = false),
            @ApiImplicitParam(name = "reserveTime", value = "预约日期(天)", required = false),
            @ApiImplicitParam(name = "openid", value = "openid", required = false),
            @ApiImplicitParam(name = "type", value = "订单状态", required = false)
    })
    @RequestMapping(value = "/pagequeryOrderPO", method = RequestMethod.POST)
    public Result<List<OrderPO>> pagequeryOrderPO(Paging page, OrderPO orderPO) {
        LogUtil.d("pagequeryOrderPO","");
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

    @ApiOperation(value = "创建预约", notes = "拦截 订单状态为待审批,和已预约的订单 ,参数参考OrderPO 注意此接口参数为集合类型")
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> createOrder(@RequestBody List<OrderPO> orderPOS) throws SCException {
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

    @RequestMapping(value = "/createOrderForBack", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> createOrderForBack(OrderPO orderPO,String pids) throws SCException {
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

    @ApiOperation(value = "审批通过", notes = "修改状态为待付款")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/approvedPass", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> approvedPass(@RequestParam String orderId) {

        OrderPO orderPO = orderDao.queryByOrderId(orderId);
        orderPO.setType(ORDER_TYPE_SUCCESS);//等待付款

        orderDao.save(orderPO);

        return ResultUtil.success(true);
    }

    @ApiOperation(value = "审批拒绝", notes = "修改状态为取消")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/approvedFail", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> approvedFail(@RequestParam String orderId) {

        OrderPO orderPO = orderDao.queryByOrderId(orderId);
        orderPO.setType(ORDER_TYPE_FAIL);//取消

        orderDao.save(orderPO);
        return ResultUtil.success(true);
    }


    @ApiOperation(value = "付款确认", notes = "修改订单为预约成功")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/payCheck", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> payCheck(@RequestParam String orderId) {

        OrderPO orderPO = orderDao.queryByOrderId(orderId);
        orderPO.setType(ORDER_TYPE_REACH);//预约成功

        orderDao.save(orderPO);
        return ResultUtil.success(true);
    }

    @ApiOperation(value = "取消订单(付款)", notes = "修改订单为取消")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/payCancel", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> payCancel(@RequestParam String orderId) {
        OrderPO orderPO = orderDao.queryByOrderId(orderId);
        orderPO.setType(ORDER_TYPE_FAIL);//取消

        orderDao.save(orderPO);
        return ResultUtil.success(true);
    }

    @ApiOperation(value = "修改订单状态", notes = "修改订单,暂不能用!!!")
    @ApiImplicitParam(name = "orderId", value = "订单号", required = true)
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
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


    @ApiOperation(value = "查询指定日期场地剩余", notes = "无")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "times", value = "日期", required = true),
            @ApiImplicitParam(name = "vid", value = "场地主键", required = true)
    })
    @RequestMapping(value = "/querDateNum", method = RequestMethod.POST)
    public Result<List<PeriodTimePO>> querDateNum(@RequestParam(value = "times") String times, @RequestParam Integer vid) {
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

    @ApiOperation(value = "查询指定日期场馆下所有场地剩余", notes = "无")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "times", value = "日期2019-05-08", required = true),
            @ApiImplicitParam(name = "type", value = "场馆主键", required = true)
    })
    @RequestMapping(value = "/getVenueSizefMonth", method = RequestMethod.POST)
    public Result<List<VenuePO>> getVenueSizefMonth(@RequestParam String times, @RequestParam int type) {
//        Date start = new Date();
//        String YYYY_MM_HH = DateUtil.addDate(new Date(), 30);
//        Date end = DateUtil.parseDate(YYYY_MM_HH + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
//        List<String> stringList = DateUtil.getBetweenDays(start, end);

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