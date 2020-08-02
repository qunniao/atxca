package com.atxca.mybatis.service.impl;

import com.atxca.Util.*;
import com.atxca.mybatis.dao.ChangGuanPeriodTimeDao;
import com.atxca.mybatis.dao.OrderDao2;
import com.atxca.mybatis.entity.*;
import com.atxca.mybatis.service.OrderService;
import com.atxca.order.PO.OrderPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.atxca.order.service.serviceImpl.OrderJpaServiceImpl.*;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderDao2 dao;

    @Resource
    private ChangGuanPeriodTimeDao timeDao;


    @Override
    public R findOrderListByWaitAndOpenId(String openid) {
        List<Order> list = dao.findOrderListByWaitAndOpenId(ORDER_TYPE_SUCCESS,openid);
        return R.ok(list,200);
    }

    @Override
    public R findOrderListByOpenId(String openid) {
        List<Order> list = dao.findOrderListByOpenId(openid);
        return R.ok(list,200);
    }

    @Override
    public R findYuYueListByChangGuanId(Integer id) {
        List<YuYuePO> list = new ArrayList<>();
        List<PianChangPO> pianChangPOList = dao.findVenueListByVhouseId2(id);
        //LOG.info("pianChangPOList size:"+pianChangPOList.size());
        List<PeriodTime> periodTimeList = timeDao.findPeriodTimeListById(id);
        //LOG.info("periodTimeList size:"+periodTimeList.size());
        int y,m,d,h=0,mi,s;
        Calendar cal= Calendar.getInstance();
        String date="";
        String mm="";
        String dd ="";
        String week = "";
        YuYuePO yuYuePO;

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date weekDate = null;
        for(int i=0;i<15;i++){
            y=cal.get(Calendar.YEAR);
            m=cal.get(Calendar.MONTH)+1;
            d=cal.get(Calendar.DATE);
            h=cal.get(Calendar.HOUR);
            mm = m+"";
            dd = d+"";
            if(m<10){
                mm="0"+m;
            }
            if(d<10){
                dd="0"+d;
            }
            date=y+"-"+mm+"-"+dd;


            //计算星期几
            try {
                weekDate = sf.parse(date);//
            }catch (Exception e){

            }
            int day=1;
            if (weekDate != null) {
                day = weekDate.getDay();
                //LogUtil.d("findChangDiList","day:"+day);
            }

            switch (day) {

                case 1:
                    week = "周一";
                    break;
                case 2:
                    week = "周二";
                    break;
                case 3:
                    week = "周三";
                    break;
                case 4:
                    week = "周四";
                    break;
                case 5:
                    week = "周五";
                    break;
                case 6:
                    week = "周六";
                    break;
                case 0:
                    week = "周日";
                    break;
            }


            yuYuePO = new YuYuePO();
            yuYuePO.setDate(date);
            yuYuePO.setDateName(m+"."+d);
            yuYuePO.setWeek(week);
            yuYuePO.setWeekName(week);
            if(i==0){
                yuYuePO.setWeekName("今天");
            }

            cal.add(Calendar.DATE, 1);//增加一天
            list.add(yuYuePO);
        }


        for(int i=0;i<list.size();i++){
            list.get(i).setPianChangList(pianChangPOList);
        }

        TimePO timePO = null;
        List<TimePO> timePOList = null;
        for(int i=0;i<list.size();i++){
            String week1 = list.get(i).getWeek();
            timePOList = new ArrayList<>();
            for(int k=0;k<periodTimeList.size();k++){
                if(periodTimeList.get(k).getPrice()==-1)continue;
                if(periodTimeList.get(k).getWeekPrice()==-1)continue;
                timePO = new TimePO();
                timePO.setBetTime(periodTimeList.get(k).getBetTime());
                timePO.setPid(periodTimeList.get(k).getPid());
                if("周一".equals(week1) || "周二".equals(week1) || "周三".equals(week1) || "周四".equals(week1) || "周五".equals(week1)){
                    timePO.setPrice(periodTimeList.get(k).getPrice());
                }
                if("周六".equals(week1) || "周日".equals(week1)){
                    timePO.setPrice(periodTimeList.get(k).getWeekPrice());
                }
                timePOList.add(timePO);
            }

            for(int p=0;p<list.get(i).getPianChangList().size();p++){
                list.get(i).getPianChangList().get(p).setTimePOList(timePOList);
            }

        }

        //LOG.info("当前小时："+h);
        //查询每个日期的每个时间段是否可预约
        PianChangPO pianChangPO;
        List<PianChangPO> list1;
        List<PeriodTimeClose> periodTimeClose;
        List<OrderPO> orderPOList;
        int is_yuyue = 0;
        for(int i=0;i<list.size();i++){
            list1 = list.get(i).getPianChangList();
            //循环片场列表
            for(int k=0;k<list1.size();k++){
                pianChangPO = list1.get(k);
                //循环时间段列表
                for(int t=0;t<pianChangPO.getTimePOList().size();t++){
                    timePO = pianChangPO.getTimePOList().get(t);
                    is_yuyue = 1;
                    //查询该日期下的该时间段是否可预约
                    //查询该时间段是否已有订单
                    orderPOList = timeDao.findOrderListByDateTimeAndVid(list.get(i).getDate(),timePO.getBetTime(),pianChangPO.getVid());
                    //LOG.info("orderPOList size:"+orderPOList.size());
                    if(null != orderPOList && orderPOList.size()>0) {
                        //有订单
                        if (orderPOList.get(0).getType() != ORDER_TYPE_FAIL) {
                            //订单状态不为预约失败
                            //LOG.info(" siz订单状态不为预约失败:"+orderPOList.get(0).getType());
                            is_yuyue = 0;
                        }
                    }
                    //LOG.info("is_yuyue000:"+is_yuyue);
                    //查询该日期时间段是否已内部关闭
                    periodTimeClose = timeDao.findPeriodTimeCloseListByVidAndDateTime(pianChangPO.getVid(),list.get(i).getDate(),timePO.getBetTime());
                    if(null != periodTimeClose && periodTimeClose.size()>0){
                        is_yuyue = 0;
                        //LOG.info("内部关闭或正在使用");
                    }
                    //LOG.info("is_yuyue111:"+is_yuyue);
                    //如果日期为今天，过去的时间不可预约
                    if(i==0){
                        String[] str = timePO.getBetTime().split("-");
                        String[] times = str[0].split(":");
                        int time = new Integer(times[0]);
                        if(time<h){
                            is_yuyue = 0;
                            //LOG.info("time<h");
                        }
                    }
                    //LOG.info("is_yuyue222:"+is_yuyue);
                    list.get(i).getPianChangList().get(k).getTimePOList().get(t).setIsYuYue(is_yuyue);
                    //LOG.info("is_yuyue333:"+list.get(i).getPianChangList().get(k).getTimePOList().get(t).getIsYuYue());
                }
            }
        }

        //LOG.info("is_yuyue333:"+list.get(0).getPianChangList().get(0).getTimePOList().get(0).getIsYuYue());
        //统计可预约的场数
        for(int i=0;i<list.size();i++){
            list1 = list.get(i).getPianChangList();
            int dateCount = 0;
            //循环片场列表
            for(int k=0;k<list1.size();k++){
                pianChangPO = list1.get(k);
                int pianchangCount = 0;
                //循环时间段列表
                for(int t=0;t<pianChangPO.getTimePOList().size();t++){
                    timePO = pianChangPO.getTimePOList().get(t);
                    //LOG.info("is_yuyue555:"+list.get(i).getPianChangList().get(k).getTimePOList().get(t).getIsYuYue());
                    if(timePO.getIsYuYue()==1){
                        pianchangCount++;
                        dateCount++;
                    }
                }
                list1.get(k).setPianchangCount(pianchangCount);
            }
            list.get(i).setDateCount(dateCount);
        }
        return R.ok(list);
    }

    @Override
    public R findYuYueDateCount(Integer id) {
        List<YuYuePO> list = new ArrayList<>();
        List<PianChangPO> pianChangPOList = dao.findVenueListByVhouseId2(id);
       // LOG.info("pianChangPOList size:"+pianChangPOList.size());
        List<PeriodTime> periodTimeList = timeDao.findPeriodTimeListById(id);
       // LOG.info("periodTimeList size:"+periodTimeList.size());
        int y,m,d,h=0,mi,s;
        Calendar cal= Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis()+8*60*60*1000);
        String date="";
        String mm="";
        String dd ="";
        String week = "";
        YuYuePO yuYuePO;

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date weekDate = null;
        h=cal.get(Calendar.HOUR);
        for(int i=0;i<7;i++){
            y=cal.get(Calendar.YEAR);
            m=cal.get(Calendar.MONTH)+1;
            d=cal.get(Calendar.DATE);
            if(m<10){
                mm="0"+m;
            }else{
                mm = m+"";
            }
            if(d<10){
                dd="0"+d;
            }else{
                dd = d+"";
            }
            date=y+"-"+mm+"-"+dd;
            LOG.info("date:"+date+" hour:"+h+" m:"+ cal.get(Calendar.MINUTE));

            //计算星期几
            try {
                weekDate = sf.parse(date);//
            }catch (Exception e){

            }
            int day=1;
            if (weekDate != null) {
                day = weekDate.getDay();
               // LogUtil.d("findChangDiList","day:"+day);
            }

            switch (day) {

                case 1:
                    week = "周一";
                    break;
                case 2:
                    week = "周二";
                    break;
                case 3:
                    week = "周三";
                    break;
                case 4:
                    week = "周四";
                    break;
                case 5:
                    week = "周五";
                    break;
                case 6:
                    week = "周六";
                    break;
                case 0:
                    week = "周日";
                    break;
            }


            yuYuePO = new YuYuePO();
            yuYuePO.setDate(date);
            yuYuePO.setDateName(m+"."+d);
            yuYuePO.setWeek(week);
            yuYuePO.setWeekName(week);
            if(i==0){
                yuYuePO.setWeekName("今天");
            }

            list.add(yuYuePO);
            if(day==0){
                break;
            }

            cal.add(Calendar.DATE, 1);//增加一天
        }

        int orderCount = 0;
        int timeCloseCount = 0;
        int dateCount = pianChangPOList.size()*periodTimeList.size();
        int count = 0;
        String dat="";
        for(int i=0;i<list.size();i++){
            dat = list.get(i).getDate();
            orderCount = dao.findOrderCountByChangGuanIdAndDateAndStateMore(dat,ORDER_TYPE_SUCCESS,id)+
                    dao.findOrderCountByChangGuanIdAndDateAndStateMore(dat,ORDER_TYPE_REACH,id);
            timeCloseCount = dao.findPeriodTimeCloseByVidAndDate(dat,id);
            count = dateCount-orderCount-timeCloseCount;
           // LOG.info("dateCount:"+dateCount+" orderCount："+orderCount+" timeCloseCount:"+timeCloseCount);
            list.get(i).setDateCount(count);
        }


        return R.ok(list,200);
    }

    @Override
    public R findYuYuePianChangList(Integer id, String reserveTime) {
        String mm="";
        String dd ="";
        int y,m,d,h=0,mi,s;
        if(null == reserveTime || "".equals(reserveTime)){
            Calendar cal= Calendar.getInstance();
            h=cal.get(Calendar.HOUR);
            y=cal.get(Calendar.YEAR);
            m=cal.get(Calendar.MONTH)+1;
            d=cal.get(Calendar.DATE);
            if(m<10){
                mm="0"+m;
            }else{
                mm = m+"";
            }
            if(d<10){
                dd="0"+d;
            }else{
                dd = d+"";
            }
            reserveTime=y+"-"+mm+"-"+dd;
        }


        String week = "";

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date weekDate = null;

            //计算星期几
            try {
                weekDate = sf.parse(reserveTime);//
            }catch (Exception e){

            }
            int day=1;
            if (weekDate != null) {
                day = weekDate.getDay();
                // LogUtil.d("findChangDiList","day:"+day);
            }


        List<PianChangPO> pianChangPOList = dao.findVenueListByVhouseId2(id);
        // LOG.info("pianChangPOList size:"+pianChangPOList.size());
        List<PeriodTime> periodTimeList = timeDao.findPeriodTimeListById(id);

        TimePO timePO = null;
        List<TimePO> timePOList = null;
        timePOList = new ArrayList<>();

        int is_yuyue  = 1;
        List<OrderPO> orderPOList;
        List<PeriodTimeClose> periodTimeClose;
        //循环片场列表
        for(int i=0;i<pianChangPOList.size();i++){
            int pianchangCount = 0;
            //循环时间段列表
            timePOList = new ArrayList<>();
            for(int k=0;k<periodTimeList.size();k++){
                if(periodTimeList.get(k).getPrice()==-1)continue;
                if(periodTimeList.get(k).getWeekPrice()==-1)continue;
                timePO = new TimePO();
                timePO.setBetTime(periodTimeList.get(k).getBetTime());
                timePO.setPid(periodTimeList.get(k).getPid());

                switch (day) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        timePO.setPrice(periodTimeList.get(k).getPrice());
                        break;
                    case 6:
                    case 0:
                        timePO.setPrice(periodTimeList.get(k).getWeekPrice());
                        break;
                }

                is_yuyue = 1;
                //查询该日期下的该时间段是否可预约
                //查询该时间段是否已有订单
                orderPOList = timeDao.findOrderListByDateTimeAndVid(reserveTime,timePO.getBetTime(),pianChangPOList.get(i).getVid());
                //LOG.info("orderPOList size:"+orderPOList.size());
                if(null != orderPOList && orderPOList.size()>0) {
                    //有订单
                    if (orderPOList.get(0).getType() != ORDER_TYPE_FAIL) {
                        //订单状态不为预约失败
                       // LOG.info(" siz订单状态不为预约失败:"+orderPOList.get(0).getType());
                        is_yuyue = 0;
                    }
                }
                // LOG.info("is_yuyue000:"+is_yuyue);
                //查询该日期时间段是否已内部关闭
                periodTimeClose = timeDao.findPeriodTimeCloseListByVidAndDateTime(pianChangPOList.get(i).getVid(),reserveTime,timePO.getBetTime());
                if(null != periodTimeClose && periodTimeClose.size()>0){
                    is_yuyue = 0;
                    //LOG.info("内部关闭或正在使用");
                }
                //LOG.info("is_yuyue111:"+is_yuyue);
                //如果日期为今天，过去的时间不可预约
                if(i==0){
                    String[] str = timePO.getBetTime().split("-");
                    String[] times = str[0].split(":");
                    int time = new Integer(times[0]);
                    if(time<h){
                        is_yuyue = 0;
                        //LOG.info("time<h");
                    }
                }
                //LOG.info("is_yuyue222:"+is_yuyue);
                //timePO.setIsYuYue(is_yuyue);
                timePO.setIsYuYue(is_yuyue);

                timePOList.add(timePO);

                if(timePO.getIsYuYue()==1){
                    pianchangCount++;
                }
            }
            pianChangPOList.get(i).setTimePOList(timePOList);
            pianChangPOList.get(i).setPianchangCount(pianchangCount);
        }

            return R.ok(pianChangPOList,200);
    }

    @Override
    public synchronized R createOrder(String name, String phone, String pids, String reserveTime, Integer vid, String openid) {
        String[] p = pids.split(",");
        Order order;

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sf.parse(reserveTime);//
        }catch (Exception e){

        }
        int day=1;
        if (d != null) {
            day = d.getDay();
            LogUtil.d("createOrder","day:"+day);
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
        LogUtil.d("createOrder","星期几："+text);

        for(int i=0;i<p.length;i++) {
            if ("".equals(p[i])) continue;
            PeriodTime periodTime = timeDao.findPeriodTimeByPid(new Integer(p[i]));
            List<PeriodTimeClose> periodTimeClose = timeDao.findPeriodTimeCloseListByVidAndDateTime(vid,reserveTime,periodTime.getBetTime());
            if(periodTimeClose!=null && periodTimeClose.size()>0){
                PeriodTimeClose periodTimeClose1 = periodTimeClose.get(0);
                if(periodTimeClose1.getType()==0){
                    return R.fail("添加失败，"+periodTime.getBetTime()+"已内部关闭");
                }else if(periodTimeClose1.getType()==1){
                    return R.fail("添加失败，"+periodTime.getBetTime()+"正在被使用");
                }
            }
            //查询该日期下的该时间段是否有订单
            List<OrderPO> orderList = timeDao.findOrderListByDateTimeAndVid(reserveTime,periodTime.getBetTime(),vid);
            if(orderList!=null && orderList.size()>0){
                OrderPO orderPO = orderList.get(0);
                if(orderPO.getType()==ORDER_TYPE_SUCCESS || orderPO.getType()==ORDER_TYPE_REACH){
                    return R.fail("添加失败，"+periodTime.getBetTime()+"已被预约");
                }
            }

            order = new Order();
            order.setVid(vid);
            order.setPid(new Integer(p[i]));
            order.setOpenid(openid);
            order.setType(ORDER_TYPE_SUCCESS);
            order.setOrderId("DL" + RandomUtil.getRandomStringByLength(4) + DateUtil.current());

            /*
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                order.setReserveTime(dateFormat.parse(reserveTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
*/
            order.setReserveTime(reserveTime);
            order.setBetTime(periodTime.getBetTime());
            order.setOpenid(openid);
            order.setName(name);
            order.setPhone(phone);
            order.setCreateTime(DateUtil.getDateToTimestamp(new Date()));//创建时间
            switch (day) {

                case 1:

                case 2:

                case 3:

                case 4:

                case 5:
                    order.setPrice(periodTime.getPrice());
                    break;
                case 6:

                case 0:
                    order.setPrice(periodTime.getWeekPrice());
                    break;
            }
            timeDao.addOrder(order);
        }
        return R.ok(200);

    }

    @Override
    public List<Order> findOrderListByReserveTimeLessThan(Date date) {
        List<Order> list = dao.findOrderListByReserveTimeLessThan(date);
        return list;
    }

    @Override
    public void updateOrderType(int oid, int type) {
        dao.updateOrderType(oid, type);
    }

    @Override
    public Order findOrderHistoryByOrderId(String orderId) {
        Order order = dao.findOrderHistoryByOrderId(orderId);
        return order;
    }

    @Override
    public void addOrderHistory(Order order) {
        dao.addOrderHistory(order);
    }

    @Override
    public void deleteOrderByOid(int oid) {
        dao.deleteOrderByOid(oid);
    }

    @Override
    public List<Order> findOrderListByAll() {
        return dao.findOrderListByAll();
    }
}

