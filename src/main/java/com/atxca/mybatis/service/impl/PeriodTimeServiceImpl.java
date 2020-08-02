package com.atxca.mybatis.service.impl;

import com.atxca.Util.*;
import com.atxca.data.PO.TypePO;
import com.atxca.mybatis.dao.ChangGuanPeriodTimeDao;
import com.atxca.mybatis.entity.ChangDiPO;
import com.atxca.mybatis.entity.Order;
import com.atxca.mybatis.entity.PeriodTime;
import com.atxca.mybatis.entity.PeriodTimeClose;
import com.atxca.mybatis.service.PeriodTimeService;


import com.atxca.order.PO.OrderPO;
import com.atxca.sportshall.PO.VenuePO;
import com.atxca.sportshall.PO.Vhouse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.atxca.order.service.serviceImpl.OrderJpaServiceImpl.*;

@Service
public class PeriodTimeServiceImpl implements PeriodTimeService {
    private static final Logger LOG = LoggerFactory.getLogger(PeriodTimeServiceImpl.class);

    @Resource
    private ChangGuanPeriodTimeDao dao;



    @Override
    public R findPeriodTimeListById(Integer id) {
        List<PeriodTime> list=  dao.findPeriodTimeListById(id);
        LOG.info("list size:"+list.size());
        return R.ok(list,1);
    }

    @Override
    public R findChangDiList(String date, String time, Integer changguan_type) {
        if("".equals(date)){
            int y,m,d,h,mi,s;
            Calendar cal= Calendar.getInstance();
            y=cal.get(Calendar.YEAR);
            m=cal.get(Calendar.MONTH)+1;
            d=cal.get(Calendar.DATE);

            String mm = m+"";
            String dd = d+"";
            if(m<10){
                mm="0"+m;
            }
            if(d<10){
                dd="0"+d;
            }
            date=y+"-"+mm+"-"+dd;
            LogUtil.d("findChangDiList","date:"+date);
        }
        /*
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sf.parse(date);//
        }catch (Exception e){

        }
        int day=1;
        if (d != null) {
            day = d.getDay();
            LogUtil.d("findChangDiList","day:"+day);
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
        LogUtil.d("findChangDiList","星期几："+text);
*/
        List<ChangDiPO> list = new ArrayList<>();
        List<Vhouse> vhouseList;
        if(changguan_type==0){
            vhouseList = dao.findVhouseListByAll();
        }else{
            vhouseList = dao.findVhouseListByType(changguan_type);
        }


        LogUtil.d("findChangDiList","vhouseList size:"+vhouseList.size());
        ChangDiPO modal;
        List<VenuePO> venuePOList;
        for(int i=0;i<vhouseList.size();i++){
            modal = new ChangDiPO();
            modal.setVhouse(vhouseList.get(i));
            //查询该场馆在该时间段是否开放
            List<PeriodTime> periodTimeList = dao.findPeriodTimeListByVidAndTime(vhouseList.get(i).getId(),time);
            if(periodTimeList.size()>0){
                //如果该场馆在时间段开放，就获取该片场列表
                venuePOList = dao.findVenueListByVhouseId(modal.getVhouse().getId());
                modal.setVenuePOList(venuePOList);
            }

            list.add(modal);
        }

        //查询该日期下的片场订单，查询状态
        for(int k=0;k<list.size();k++){
            List<VenuePO> venuePOList1 = list.get(k).getVenuePOList();
            if(null == venuePOList1)continue;

            for(int v=0;v<venuePOList1.size();v++){
                List<OrderPO> orderPOList = dao.findOrderListByDateTimeAndVid(date,time,venuePOList1.get(v).getVid());
                if(null == orderPOList || orderPOList.size()==0)continue;

                list.get(k).getVenuePOList().get(v).setYuyue_state(orderPOList.get(0).getType());
            }
        }


        //查询片场关闭的日期和时间段，判断片场当前时间段状态

        List<PeriodTimeClose> periodTimeCloseList;
        for(int k=0;k<list.size();k++) {
            if(null == list.get(k).getVenuePOList())continue;//该场馆在该时间段没有开放片场

            List<VenuePO> venuePOList1 = list.get(k).getVenuePOList();
            for(int v=0;v<venuePOList1.size();v++){
                periodTimeCloseList = dao.findPeriodTimeCloseListByVid(venuePOList1.get(v).getVid());
                for(int p=0;p<periodTimeCloseList.size();p++){
                    //一次关闭
                    if(periodTimeCloseList.get(p).getType()==0){
                        //关闭日期为当前选择的日期
                        if(periodTimeCloseList.get(p).getClose_date().equals(date)){
                            //关闭时间段为当前选择的时间段
                            if(periodTimeCloseList.get(p).getClose_time().equals(time)){
                                list.get(k).getVenuePOList().get(v).setYuyue_state(-1);
                                LOG.info("已内部一次关闭 vid:"+list.get(k).getVenuePOList().get(v).getVid()+" 关闭日期："+date+" 关闭时间段："+time);
                            }
                        }
                    }else if(periodTimeCloseList.get(p).getType()==1){
                        //关闭日期为当前选择的日期
                        if(periodTimeCloseList.get(p).getClose_date().equals(date)){
                            //关闭时间段为当前选择的时间段
                            if(periodTimeCloseList.get(p).getClose_time().equals(time)){
                                list.get(k).getVenuePOList().get(v).setYuyue_state(7);
                                LOG.info("已内部正在使用中 vid:"+list.get(k).getVenuePOList().get(v).getVid()+" 日期："+date+" 时间段："+time);
                            }
                        }
                    }
                    //长期关闭
                    /*
                    else if(periodTimeCloseList.get(p).getType()==-1){
                        //如果关闭的星期一样
                        if(periodTimeCloseList.get(p).getClose_date().equals(text)){
                            //关闭时间段为当前选择的时间段
                            if(periodTimeCloseList.get(p).getClose_time().equals(time)){
                                list.get(k).getVenuePOList().get(v).setYuyue_state(-1);
                                LOG.info("已内部长期关闭 vid:"+list.get(k).getVenuePOList().get(v).getVid()+" 关闭日期："+date+" 关闭时间段："+time);
                            }
                        }
                    }
                    */
                }
            }
        }
        LOG.info("list size:"+list.size());
        return R.ok(list);
    }

    @Override
    public R updatePianChangState(String date, String time, Integer id, Integer old_state, Integer new_state) {
        if(old_state==new_state){
            return R.fail("修改失败：新旧状态一样");
        }

        if("".equals(date)){
            int y,m,d,h,mi,s;
            Calendar cal= Calendar.getInstance();
            y=cal.get(Calendar.YEAR);
            m=cal.get(Calendar.MONTH)+1;
            d=cal.get(Calendar.DATE);
            date=y+"-"+m+"-"+d;
            LogUtil.d("findChangDiList","date:"+date);
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sf.parse(date);//
        }catch (Exception e){

        }
        int day=1;
        if (d != null) {
            day = d.getDay();
            LogUtil.d("findChangDiList","day:"+day);
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
        LogUtil.d("findChangDiList","星期几："+text);

        List<OrderPO> orderPOList = dao.findOrderListByDateTimeAndVid(date,time,id);

        //如果新状态为正在被使用
        if(new_state == 7) {

            if (old_state == -1) {
                //之前状态是内部关闭，删除该片场内部关闭数据；
                dao.deletePeriodTimeCloseByVidAndDateTime(id, date, time);
            }

            if (orderPOList.size() > 0) {
                //已有预约
                OrderPO orderPO = orderPOList.get(0);
                if (orderPO.getType() != ORDER_TYPE_FAIL) {
                    //如果预约状态不为预约失败，则将预约状态改为已到场
                    orderPO.setType(ORDER_TYPE_REACH);
                    dao.updateOrderState(orderPO);
                    return R.ok();
                }
            }

            PeriodTimeClose periodTimeClose = new PeriodTimeClose();
            periodTimeClose.setClose_date(date);
            periodTimeClose.setClose_time(time);
            periodTimeClose.setType(1);
            periodTimeClose.setVenues_id(id);

            dao.addPeriodTimeClose(periodTimeClose);
            return R.ok();
        }
            /*
                Vhouse vhouse = dao.findVhouseByVnenueId(id);
                //创建一个虚拟订单
                Order orderPO1 = new Order();
                orderPO1.setType(ORDER_TYPE_REACH);
                orderPO1.setOrderId("zc" + RandomUtil.getRandomStringByLength(4) + DateUtil.current());

                List<PeriodTime> periodTimePO2 = dao.findPeriodTimeListByVidAndTime(vhouse.getId(),time);
                int price = 0;
                int pid = 0;

                if(periodTimePO2.size()>0){
                    pid = periodTimePO2.get(0).getPid();
                    switch (day) {

                        case 1:

                        case 2:

                        case 3:

                        case 4:

                        case 5:
                            orderPO1.setPrice(periodTimePO2.get(0).getPrice());
                            break;
                        case 6:

                        case 0:
                            orderPO1.setPrice(periodTimePO2.get(0).getWeekPrice());
                            break;
                    }
                }
                orderPO1.setPid(pid);
                orderPO1.setType(ORDER_TYPE_REACH);
                orderPO1.setOpenid("0");
                orderPO1.setVid(id);
                orderPO1.setName("匿名");
                orderPO1.setPhone("-");
                orderPO1.setBetTime(time);//预约时间段
                orderPO1.setReserveTime(date);//指定日期
                orderPO1.setCreateTime(DateUtil.getDateToTimestamp(new Date()));//创建时间
                orderPO1.setRemakes("管理员通过正在被使用状态创建");

                dao.saveOrder(orderPO1);

            return R.ok();
        }
        */
        //如果新状态为内部关闭
        //内部关闭表中加一条数据
        if(new_state==-1){
            PeriodTimeClose periodTimeClose = new PeriodTimeClose();
            periodTimeClose.setClose_date(date);
            periodTimeClose.setClose_time(time);
            periodTimeClose.setType(0);
            periodTimeClose.setVenues_id(id);

            //避免重复添加，删除已有的
            dao.deletePeriodTimeCloseByVidAndDateTime(periodTimeClose.getVenues_id(),periodTimeClose.getClose_date(),periodTimeClose.getClose_time());
            dao.addPeriodTimeClose(periodTimeClose);
        }
        //如果新状态为空置可预约
        else if(new_state==0){
            //查询内部关闭表中该片场该日期该时间段是否有数据,并删除
            dao.deletePeriodTimeCloseByVidAndDateTime(id,date,time);
        }

        //查询该时间段是否有订单,有的话状态改为预约失败

        if(orderPOList.size()>0){
            for(int i=0;i<orderPOList.size();i++){
                orderPOList.get(i).setType(ORDER_TYPE_FAIL);
                dao.updateOrderState(orderPOList.get(i));
            }

        }
        return R.ok();
    }

    @Override
    public R findChangGuanTypeList() {
        List<TypePO> list=  dao.findChangGuanTypeList();
        LOG.info("list size:"+list.size());
        return R.ok(list,1);
    }

    @Override
    public R deleteChangGuanById(Integer id) {
        dao.deleteChangGuanById(id);
        return R.ok(1);
    }

    @Override
    public R findChangGuanById(Integer id) {
        Vhouse item = dao.findChangGuanById(id);
        return R.ok(item);
    }

    @Override
    public R findPianChangList(Integer id) {
        List<VenuePO> list = dao.findVenueListByVhouseId(id);
        return R.ok(list);
    }

    @Override
    public R updateChangGuan(Integer id, String name) {
        Vhouse item = dao.findChangGuanById(id);
        item.setName(name);
        dao.updateChangGuan(item);
        return R.ok();
    }

    @Override
    public R updatePianChang(Integer id, String name) {
        VenuePO item = dao.findVenueById(id);
        item.setVName(name);
        dao.updatePianChang(item);
        return R.ok();
    }


    @Override
    public R updatePeriodTime(Integer pid, String betTime, Integer sort, Double price, Double weekPrice) {
        PeriodTime item = dao.findPeriodTimeByPid(pid);
        item.setBetTime(betTime);
        item.setSort(sort);
        item.setPrice(price);
        item.setWeekPrice(weekPrice);
        dao.updatePeriodTime(item);
        return R.ok();
    }

    @Override
    public R addCloseDateTimeByMore(String times, String ids, String dates) {
        String[] idArray = ids.split(",");
        String[] datesArray = dates.split(",");
        String[] timessArray = times.split(",");

        PeriodTimeClose periodTimeClose;
        for(int i=0;i<idArray.length;i++){
            for(int d = 0;d<datesArray.length;d++){

                periodTimeClose = new PeriodTimeClose();
                periodTimeClose.setVenues_id(new Integer(idArray[i]));
                periodTimeClose.setClose_date(datesArray[d]);
                periodTimeClose.setType(0);
                PeriodTime periodTime;
                for(int t=0;t<timessArray.length;t++){
                    periodTime = dao.findPeriodTimeByPid(new Integer(timessArray[t]));
                    periodTimeClose.setClose_time(periodTime.getBetTime());
                    periodTimeClose.setPid(new Integer(periodTime.getPid()));

                    //避免重复添加，删除已有的
                    dao.deletePeriodTimeCloseByVidAndDateTime(periodTimeClose.getVenues_id(),periodTimeClose.getClose_date(),periodTimeClose.getClose_time());
                    dao.addPeriodTimeClose(periodTimeClose);
                }

            }
        }
        return R.ok();
    }

    @Override
    public R findOrderList(String phone, String name, String date, String time, String changdi, String state, Integer page) {
        List<Order> list = new ArrayList<>();
        if("0".equals(changdi)){
            //全部场馆
            if("0".equals(state)){
                //全部状态
                if("".equals(date) && "".equals(time)){
                    LogUtil.d("findOrderList","全部场馆 全部状态");
                    list = dao.findOrderListByAll((page-1)*20,20,phone,name);
                }else{
                    if(!"".equals(date) && !"".equals(time)){
                        list = dao.findOrderListByAllAndDateTime(date,time,(page-1)*20,20,phone,name);
                    }else if(!"".equals(date)){
                        list = dao.findOrderListByAllAndDate(date,(page-1)*20,20,phone,name);
                    }else if(!"".equals(time)){
                        list = dao.findOrderListByAllAndTime(time,(page-1)*20,20,phone,name);
                    }

                }

            }else{
                //某个状态
                if("".equals(date) && "".equals(time)){
                    LogUtil.d("findOrderList","全部场馆 某个状态");
                    list = dao.findOrderListByAllAndState(new Integer(state),(page-1)*20,20,phone,name);
                }else{
                    if(!"".equals(date) && !"".equals(time)){
                        list = dao.findOrderListByAllAndStateAndDateTime(state,date,time,(page-1)*20,20,phone,name);
                    }else if(!"".equals(date)){
                        list = dao.findOrderListByAllAndStateAndDate(state,date,(page-1)*20,20,phone,name);
                    }else if(!"".equals(time)){
                        list = dao.findOrderListByAllAndStateAndTime(state,time,(page-1)*20,20,phone,name);
                    }

                }

                LogUtil.d("findOrderList","全部场馆 某个状态");

            }
        }else{
            if("0".equals(state)){
                //全部状态
                if("".equals(date) && "".equals(time)){
                    LogUtil.d("findOrderList","某个场馆 全部状态");
                    list = dao.findOrderListByChangGuan(changdi,(page-1)*20,20,phone,name);
                }else{
                    if(!"".equals(date) && !"".equals(time)){
                        list = dao.findOrderListByChangGuanAndDateTime(changdi,date,time,(page-1)*20,20,phone,name);
                    }else if(!"".equals(date)){
                        list = dao.findOrderListByChangGuanAndDate(changdi,date,(page-1)*20,20,phone,name);
                    }else if(!"".equals(time)){
                        list = dao.findOrderListByChangGuanAndTime(changdi,time,(page-1)*20,20,phone,name);
                    }

                }

            }else{
                LogUtil.d("findOrderList","某个场馆 某个状态");

                if("".equals(date) && "".equals(time)){
                    list = dao.findOrderListByChangGuanAndState(new Integer(state),changdi,(page-1)*20,20,phone,name);
                }else{
                    if(!"".equals(date) && !"".equals(time)){
                        list = dao.findOrderListByChangGuanAndStateAndDateTime(state,changdi,date,time,(page-1)*20,20,phone,name);
                    }else if(!"".equals(date)){
                        list = dao.findOrderListByChangGuanAndStateAndDate(state,changdi,date,(page-1)*20,20,phone,name);
                    }else if(!"".equals(time)){
                        list = dao.findOrderListByChangGuanAndStateAndTime(state,changdi,time,(page-1)*20,20,phone,name);
                    }

                }
            }
        }
        LogUtil.i("list:"+list.size());
        return R.ok(list);
    }

    @Override
    public R findChangGuanList() {
        List<Vhouse> list = dao.findVhouseListByAll();
        return R.ok(list);
    }

    @Override
    public R deleteOrderById(Integer id) {
        dao.deleteOrderById(id);
        return R.ok();
    }

    @Override
    public R addPianChangByMore(Integer id, String name, Integer num) {
        String pianchang_name = name;
        String[] str = name.split("###");
        if(str.length>0){
            pianchang_name = "";
            for(int i=0;i<str.length;i++){
                LOG.info("str i:"+i+" value:"+str[i]);
                if("".equals(str[i]))continue;
                pianchang_name+=str[i];
            }
        }
        VenuePO item;
        if(num==1){
            item = new VenuePO();
            item.setVName(pianchang_name);
            item.setGroups(id);
            dao.addPianChang(item);
        }else if(num>1){
            int n=0;
            try {
                n = new Integer(str[1]);
            }catch (Exception e){
                return R.fail("井号中间必须为数字");
            }
            for(int i=0;i<num;i++){
                item = new VenuePO();
                item.setGroups(id);
                item.setVName(str[0]+""+n+""+str[2]);
                dao.addPianChang(item);
                n++;
            }
        }
        return R.ok();
    }

    @Override
    public R deletePianChangByMore(String ids) {
        String[] idArray = ids.split(",");
        for(int i=0;i<idArray.length;i++){
            if("".equals(idArray[i]))continue;
            dao.deletePianChangById(new Integer(idArray[i]));
        }
        return R.ok();
    }

    @Override
    public R deletePeriodTimeByMore(String ids) {
        String[] idArray = ids.split(",");
        for(int i=0;i<idArray.length;i++){
            if("".equals(idArray[i]))continue;
            dao.deletePeriodTimeById(new Integer(idArray[i]));
        }
        return R.ok();
    }

    @Override
    public R addPeriodTimeByMore(Integer id, String content, Double price, Double weekPrice, Integer sort) {
        String[] idArray = content.split(",");
        PeriodTime item;
        for(int i=0;i<idArray.length;i++){
            if("".equals(idArray[i]))continue;
            item = new PeriodTime();
            item.setVhouse_id(id);
            item.setBetTime(idArray[i]);
            item.setPrice(price);
            item.setWeekPrice(weekPrice);
            item.setSort(sort);
            item.setVid(0);
            dao.addPeriodTime(item);
        }
        return R.ok();
    }

    @Override
    public R findOrderListBySuccess(String start_date, String end_date, String changdi,String state, Integer page) {
        List<Order> list = new ArrayList<>();
        //全部场馆
        if("0".equals(changdi)){
           //开始日期和结束日期都为空，查询全部
           if("".equals(start_date) && "".equals(end_date)){
               list = dao.findOrderListByAllAndState(ORDER_TYPE_REACH,(page-1)*20,2000, "", "");
           }else{
               list = dao.findOrderListByAllAndStateStartDateEndDate(ORDER_TYPE_REACH,start_date,end_date,(page-1)*20,2000);
           }

        }else{

                LogUtil.d("findOrderList","某个场馆 某个状态");

            if("".equals(start_date) && "".equals(end_date)){
                list = dao.findOrderListByChangGuanAndState(ORDER_TYPE_REACH,changdi,(page-1)*20,2000, "", "");
            }else{
                list = dao.findOrderListByChangGuanAndStateStartDateEndDate(ORDER_TYPE_REACH,changdi,start_date,end_date,(page-1)*20,2000);
            }

        }
        LogUtil.i("list:"+list.size());
        return R.ok(list);
    }


    @Override
    public R findOrderListBySuccess2(String start_date, String end_date, String changdi,Integer state, Integer page) {
        List<Order> list = new ArrayList<>();
        if(!start_date.equals("")){
            String strs[] = start_date.split("-");
            start_date=strs[0]+strs[1]+strs[2];
        }
        if(!end_date.equals("")){
            String strs[] = end_date.split("-");
            end_date=strs[0]+strs[1]+strs[2];
        }
        System.out.println("start_date:"+start_date+" end_date:"+end_date);
        //全部场馆
        if("0".equals(changdi)){
            //开始日期和结束日期都为空，查询全部
            if("".equals(start_date) && "".equals(end_date)){
                if(state == 0){
                    list = dao.findOrderListByAllAndStateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,(page-1)*20,20, "", "");
                }else if(state == 7){
                    list = dao.findOrderListByAllAndState(ORDER_TYPE_REACH,(page-1)*20,20, "", "");
                }else if(state == 6){
                    list = dao.findOrderListByAllAndState(ORDER_TYPE_REACH_ALIPAY,(page-1)*20,20, "", "");
                }

            }else{
                if(state == 0){
                    list = dao.findOrderListByAllAndStateStartDateEndDateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,start_date,end_date,(page-1)*20,20);
                }else if(state == 7){
                    list = dao.findOrderListByAllAndStateStartDateEndDate(ORDER_TYPE_REACH,start_date,end_date,(page-1)*20,20);
                }else if(state == 6){
                    list = dao.findOrderListByAllAndStateStartDateEndDate(ORDER_TYPE_REACH_ALIPAY,start_date,end_date,(page-1)*20,20);
                }

            }

        }else{

            LogUtil.d("findOrderList","某个场馆 某个状态");

            if("".equals(start_date) && "".equals(end_date)){
                if(state == 0){
                    list = dao.findOrderListByChangGuanAndStateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,changdi,(page-1)*20,2000, "", "");
                }else if(state == 7){
                    list = dao.findOrderListByChangGuanAndState(ORDER_TYPE_REACH,changdi,(page-1)*20,2000, "", "");
                }else if(state == 6){
                    list = dao.findOrderListByChangGuanAndState(ORDER_TYPE_REACH_ALIPAY,changdi,(page-1)*20,2000, "", "");

                }

            }else{
                if(state == 0){
                    list = dao.findOrderListByChangGuanAndStateStartDateEndDateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,changdi,start_date,end_date,(page-1)*20,2000);
                }else if(state == 7){
                    list = dao.findOrderListByChangGuanAndStateStartDateEndDate(ORDER_TYPE_REACH,changdi,start_date,end_date,(page-1)*20,2000);
                }else if(state == 6){
                    list = dao.findOrderListByChangGuanAndStateStartDateEndDate(ORDER_TYPE_REACH_ALIPAY,changdi,start_date,end_date,(page-1)*20,2000);
                }

            }

        }
        LogUtil.i("list:"+list.size());
        return R.ok(list);
    }


    @Override
    public R daochuExcel2(String start_date, String end_date, String changdi, Integer state, HttpSession session) {
        List<Order> list = new ArrayList<>();
        int page = 1;
        //全部场馆
        if("0".equals(changdi)){
            //开始日期和结束日期都为空，查询全部
            if("".equals(start_date) && "".equals(end_date)){
                if(state == 0){
                    list = dao.findOrderListByAllAndStateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,(page-1)*20,2000000, "", "");
                }else if(state == 7){
                    list = dao.findOrderListByAllAndState(ORDER_TYPE_REACH,(page-1)*20,2000000, "", "");
                }else if(state == 6){
                    list = dao.findOrderListByAllAndState(ORDER_TYPE_REACH_ALIPAY,(page-1)*20,2000000, "", "");
                }
               // list = dao.findOrderListByAllAndState(ORDER_TYPE_REACH,0,2000000, "", "");
            }else{
                if(state == 0){
                    list = dao.findOrderListByAllAndStateStartDateEndDateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,start_date,end_date,(page-1)*20,2000000);
                }else if(state == 7){
                    list = dao.findOrderListByAllAndStateStartDateEndDate(ORDER_TYPE_REACH,start_date,end_date,(page-1)*20,2000000);
                }else if(state == 6){
                    list = dao.findOrderListByAllAndStateStartDateEndDate(ORDER_TYPE_REACH_ALIPAY,start_date,end_date,(page-1)*20,2000000);
                }

                //list = dao.findOrderListByAllAndStateStartDateEndDate(ORDER_TYPE_REACH,start_date,end_date,0,2000000);
            }

        }else{

            LogUtil.d("findOrderList","某个场馆 某个状态");

            if("".equals(start_date) && "".equals(end_date)){
                if(state == 0){
                    list = dao.findOrderListByChangGuanAndStateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,changdi,(page-1)*20,2000000, "", "");
                }else if(state == 7){
                    list = dao.findOrderListByChangGuanAndState(ORDER_TYPE_REACH,changdi,(page-1)*20,2000000, "", "");
                }else if(state == 6){
                    list = dao.findOrderListByChangGuanAndState(ORDER_TYPE_REACH_ALIPAY,changdi,(page-1)*20,2000000, "", "");

                }
              //  list = dao.findOrderListByChangGuanAndState(ORDER_TYPE_REACH,changdi,0,2000000, "", "");
            }else{
                if(state == 0){
                    list = dao.findOrderListByChangGuanAndStateStartDateEndDateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,changdi,start_date,end_date,(page-1)*20,2000000);
                }else if(state == 7){
                    list = dao.findOrderListByChangGuanAndStateStartDateEndDate(ORDER_TYPE_REACH,changdi,start_date,end_date,(page-1)*20,2000000);
                }else if(state == 6){
                    list = dao.findOrderListByChangGuanAndStateStartDateEndDate(ORDER_TYPE_REACH_ALIPAY,changdi,start_date,end_date,(page-1)*20,2000000);
                }
               // list = dao.findOrderListByChangGuanAndStateStartDateEndDate(ORDER_TYPE_REACH,changdi,start_date,end_date,0,2000000);
            }

        }

        List<String> heads = new ArrayList<>();
        heads.add("编号");
        heads.add("场馆");
        heads.add("片场");
        heads.add("会员名称");
        heads.add("会员电话");
        heads.add("预约日期");
        heads.add("预约时间段");
        heads.add("价格");
        heads.add("状态");
        ByteArrayOutputStream baos = null;
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("报表");
        ExcelUtil.headList(heads, sheet);

        double sumprice = 0;
        double sumprice_xianjin = 0;
        double sumprice_alipay = 0;
        int cell = 1;
        for (Order order : list) {
            HSSFRow headerRow = sheet.createRow(cell);// 行
            int row = 0;
            List cloums = new ArrayList<>();
            cloums.add(order.getOid());//编号
            if(null == order.getChangguan_name()){
                cloums.add("-");//场馆名
            }else{
                cloums.add(order.getChangguan_name());//场馆名
            }

            if(null == order.getPianchang_name()){
                cloums.add("-");//片场
            }else{
                cloums.add(order.getPianchang_name());//片场
            }

            cloums.add(order.getName());//名称
            cloums.add(order.getPhone());//电话
            cloums.add(order.getReserveTime());//日期
            cloums.add(order.getBetTime());//时间段
            cloums.add(order.getPrice() + "");//价格
            if (order.getOpenid() == null) {
                cloums.add("现场到场");
            } else {
                if(order.getType()==7){
                    cloums.add("现金到场");
                }else if(order.getType()==6){
                    cloums.add("支付宝到场");
                }

            }
            sumprice = sumprice + order.getPrice();
            if(order.getType()==7){
                sumprice_xianjin = sumprice_xianjin + order.getPrice();
            }else if(order.getType()==6){
                sumprice_alipay = sumprice_alipay + order.getPrice();
            }

            for (Object aaa : cloums) {
                HSSFCell cell0 = headerRow.createCell(row); // 列
                cell0.setCellValue(aaa.toString());
                row += 1;
            }
            cell += 1;
        }
        cell = cell + 1;
        HSSFRow headerRow = sheet.createRow(cell);// 行
        HSSFCell cell0 = headerRow.createCell(0); // 列
        cell0.setCellValue("总计");

        HSSFCell cell02 = headerRow.createCell(1); // 列
        cell02.setCellValue("订单数量:" + list.size());

        HSSFCell cell3 = headerRow.createCell(2); // 列
        cell3.setCellValue("总销售额" + sumprice);

        HSSFCell cell4 = headerRow.createCell(3); // 列
        cell4.setCellValue("现金销售额" + sumprice_xianjin);

        HSSFCell cell5 = headerRow.createCell(4); // 列
        cell5.setCellValue("支付宝销售额" + sumprice_alipay);

        String fileName="健身报表"+start_date+"-"+(int)(System.currentTimeMillis()/1000)+".xls";
        String path = session.getServletContext().getRealPath(Constants.FILE_PATH);
        String downloadPath=Constants.SERVER_HOST+Constants.FILE_PATH+fileName;
        File file =null;

           // path = ResourceUtils.getURL("classpath:").getPath();

            File FilePath = new File(path);
            if(!FilePath.exists()){
                FilePath.mkdirs();
            }

            // 生成文件
            file = new File(path,fileName);
            try {
                file.createNewFile();
                if(!file.exists()){
                    file.createNewFile();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



        LOG.info("path:"+path+fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);//写出文件
            fos.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {

        }


       // FastDFSClient fastDFSClient = new FastDFSClient();
       // String url = fastDFSClient.uploadFile(baos.toByteArray(), "xls");

        return R.ok(downloadPath);
    }

    @Override
    public R findOrderListByPhone(String phone) {
        List<Order> list = dao.findOrderListByPhone(phone);
        return R.ok(list);
    }

    @Override
    public R findOrderListByName(String name) {
        List<Order> list = dao.findOrderListByName(name);
        return R.ok(list);
    }

    @Override
    public R createOrder(String name, String phone, String pids, String reserveTime, Integer vid, Integer type) {
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

        for(int i=0;i<p.length;i++) {
            if ("".equals(p[i])) continue;
            PeriodTime periodTime = dao.findPeriodTimeByPid(new Integer(p[i]));
            List<PeriodTimeClose> periodTimeClose = dao.findPeriodTimeCloseListByVidAndDateTime(vid,reserveTime,periodTime.getBetTime());
            if(periodTimeClose!=null && periodTimeClose.size()>0){
                PeriodTimeClose periodTimeClose1 = periodTimeClose.get(0);
                if(periodTimeClose1.getType()==0){
                    return R.fail("添加失败，"+periodTime.getBetTime()+"已内部关闭");
                }else if(periodTimeClose1.getType()==1){
                    return R.fail("添加失败，"+periodTime.getBetTime()+"正在被使用");
                }
            }
            //查询该日期下的该时间段是否有订单
            List<OrderPO> orderList = dao.findOrderListByDateTimeAndVid(reserveTime,periodTime.getBetTime(),vid);
            if(orderList!=null && orderList.size()>0){
                OrderPO orderPO = orderList.get(0);
                if(orderPO.getType()==ORDER_TYPE_SUCCESS || orderPO.getType()==ORDER_TYPE_REACH){
                    return R.fail("添加失败，"+periodTime.getBetTime()+"已被预约");
                }
            }

            order = new Order();
            order.setVid(vid);
            order.setPid(new Integer(p[i]));
            order.setType(type);
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
            order.setOpenid("0");
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
            dao.addOrder(order);
        }
        return R.ok(200);
    }

    @Override
    public R refreshOrder() {
        String msg = "没有发现未到场人员";
        List<Order> list = dao.findOrderListByAllAndState(ORDER_TYPE_SUCCESS,0,100000, "", "");
        if(null == list || list.size()==0){
            return R.ok(msg);
        }


        String name = "";
        int num=0;
        int nowTime = (int)(System.currentTimeMillis()/1000);
        String format="yyyy-MM-dd HH:mm:ss";
        String date="";
        int orderTime = 0;
        for(int i=0;i<list.size();i++){
            Order order = list.get(i);
            if(order.getType()!=ORDER_TYPE_SUCCESS)continue;

            String[] str = order.getBetTime().split("-");
            String[] times = str[0].split(":");
            int time = new Integer(times[0]);
            if(time<10){
                date = order.getReserveTime()+" "+"0"+time+":00:00";
            }else{
                date = order.getReserveTime()+" "+time+":00:00";
            }
            LOG.info("date:"+date);
            orderTime = FormatUtil.date2TimeStamp(date,format);
            LOG.info("orderTime:"+orderTime);
            if(orderTime<nowTime){
                //预约时间小于当前时间
                order.setType(ORDER_TYPE_FAIL);
                dao.updateOrderState2(order);
                name+=order.getName()+",";
                num++;
            }

        }

        if(num>0){
            msg = "已发现 "+num+" 个未到场人员，已将"+name+"订单状态修改为预约失败";
        }
        return R.ok(msg);
    }

    @Override
    public R findOrderListHistory(String phone, String name, String date, String time, String changdi, String state, Integer page) {
        List<Order> list = new ArrayList<>();
        if("0".equals(changdi)){
            //全部场馆
            if("0".equals(state)){
                //全部状态
                if("".equals(date) && "".equals(time)){
                    LogUtil.d("findOrderListHistory","全部场馆 全部状态");
                    list = dao.findOrderListHistoryByAll((page-1)*20,20,phone,name);
                }else{
                    if(!"".equals(date) && !"".equals(time)){
                        list = dao.findOrderListHistoryByAllAndDateTime(date,time,(page-1)*20,20,phone,name);
                    }else if(!"".equals(date)){
                        list = dao.findOrderListHistoryByAllAndDate(date,(page-1)*20,20,phone,name);
                    }else if(!"".equals(time)){
                        list = dao.findOrderListHistoryByAllAndTime(time,(page-1)*20,20,phone,name);
                    }

                }

            }else{
                //某个状态
                if("".equals(date) && "".equals(time)){
                    LogUtil.d("findOrderListHistory","全部场馆 某个状态");
                    list = dao.findOrderListHistoryByAllAndState(new Integer(state),(page-1)*20,20,phone,name);
                }else{
                    if(!"".equals(date) && !"".equals(time)){
                        list = dao.findOrderListHistoryByAllAndStateAndDateTime(state,date,time,(page-1)*20,20,phone,name);
                    }else if(!"".equals(date)){
                        list = dao.findOrderListHistoryByAllAndStateAndDate(state,date,(page-1)*20,20,phone,name);
                    }else if(!"".equals(time)){
                        list = dao.findOrderListHistoryByAllAndStateAndTime(state,time,(page-1)*20,20,phone,name);
                    }

                }

                LogUtil.d("findOrderListHistory","全部场馆 某个状态");

            }
        }else{
            if("0".equals(state)){
                //全部状态
                if("".equals(date) && "".equals(time)){
                    LogUtil.d("findOrderListHistory","某个场馆 全部状态");
                    list = dao.findOrderListHistoryByChangGuan(changdi,(page-1)*20,20,phone,name);
                }else{
                    if(!"".equals(date) && !"".equals(time)){
                        list = dao.findOrderListHistoryByChangGuanAndDateTime(changdi,date,time,(page-1)*20,20,phone,name);
                    }else if(!"".equals(date)){
                        list = dao.findOrderListHistoryByChangGuanAndDate(changdi,date,(page-1)*20,20,phone,name);
                    }else if(!"".equals(time)){
                        list = dao.findOrderListHistoryByChangGuanAndTime(changdi,time,(page-1)*20,20,phone,name);
                    }

                }

            }else{
                LogUtil.d("findOrderListHistory","某个场馆 某个状态");

                if("".equals(date) && "".equals(time)){
                    list = dao.findOrderListHistoryByChangGuanAndState(new Integer(state),changdi,(page-1)*20,20,phone,name);
                }else{
                    if(!"".equals(date) && !"".equals(time)){
                        list = dao.findOrderListHistoryByChangGuanAndStateAndDateTime(state,changdi,date,time,(page-1)*20,20,phone,name);
                    }else if(!"".equals(date)){
                        list = dao.findOrderListHistoryByChangGuanAndStateAndDate(state,changdi,date,(page-1)*20,20,phone,name);
                    }else if(!"".equals(time)){
                        list = dao.findOrderListHistoryByChangGuanAndStateAndTime(state,changdi,time,(page-1)*20,20,phone,name);
                    }

                }
            }
        }
        LogUtil.i("list:"+list.size());
        return R.ok(list);
    }

    @Override
    public R findOrderListHistoryByPhone(String phone) {
        List<Order> list = dao.findOrderListHistoryByPhone(phone);
        return R.ok(list);
    }

    @Override
    public R findOrderListHistoryByName(String name) {
        List<Order> list = dao.findOrderListHistoryByName(name);
        return R.ok(list);
    }

    @Override
    public R deleteOrderHistoryById(Integer id) {
        dao.deleteOrderHistoryById(id);
        return R.ok();
    }

    @Override
    public R exportOrderListHistory(String start_date, String end_date, String changdi, Integer state, HttpSession session) {
        List<Order> list = new ArrayList<>();
        int page = 1;
        //全部场馆
        if("0".equals(changdi)){
            //开始日期和结束日期都为空，查询全部
            if("".equals(start_date) && "".equals(end_date)){
                if(state == 0){
                    list = dao.findOrderListByAllAndStateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,(page-1)*20,2000000, "", "");
                }else if(state == 7){
                    list = dao.findOrderListByAllAndState(ORDER_TYPE_REACH,(page-1)*20,2000000, "", "");
                }else if(state == 6){
                    list = dao.findOrderListByAllAndState(ORDER_TYPE_REACH_ALIPAY,(page-1)*20,2000000, "", "");
                }
                // list = dao.findOrderListByAllAndState(ORDER_TYPE_REACH,0,2000000, "", "");
            }else{
                if(state == 0){
                    list = dao.findOrderListByAllAndStateStartDateEndDateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,start_date,end_date,(page-1)*20,2000000);
                }else if(state == 7){
                    list = dao.findOrderListByAllAndStateStartDateEndDate(ORDER_TYPE_REACH,start_date,end_date,(page-1)*20,2000000);
                }else if(state == 6){
                    list = dao.findOrderListByAllAndStateStartDateEndDate(ORDER_TYPE_REACH_ALIPAY,start_date,end_date,(page-1)*20,2000000);
                }

                //list = dao.findOrderListByAllAndStateStartDateEndDate(ORDER_TYPE_REACH,start_date,end_date,0,2000000);
            }

        }else{

            LogUtil.d("findOrderList","某个场馆 某个状态");

            if("".equals(start_date) && "".equals(end_date)){
                if(state == 0){
                    list = dao.findOrderListByChangGuanAndStateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,changdi,(page-1)*20,2000000, "", "");
                }else if(state == 7){
                    list = dao.findOrderListByChangGuanAndState(ORDER_TYPE_REACH,changdi,(page-1)*20,2000000, "", "");
                }else if(state == 6){
                    list = dao.findOrderListByChangGuanAndState(ORDER_TYPE_REACH_ALIPAY,changdi,(page-1)*20,2000000, "", "");

                }
                //  list = dao.findOrderListByChangGuanAndState(ORDER_TYPE_REACH,changdi,0,2000000, "", "");
            }else{
                if(state == 0){
                    list = dao.findOrderListByChangGuanAndStateStartDateEndDateSuccess(ORDER_TYPE_REACH,ORDER_TYPE_REACH_ALIPAY,changdi,start_date,end_date,(page-1)*20,2000000);
                }else if(state == 7){
                    list = dao.findOrderListByChangGuanAndStateStartDateEndDate(ORDER_TYPE_REACH,changdi,start_date,end_date,(page-1)*20,2000000);
                }else if(state == 6){
                    list = dao.findOrderListByChangGuanAndStateStartDateEndDate(ORDER_TYPE_REACH_ALIPAY,changdi,start_date,end_date,(page-1)*20,2000000);
                }
                // list = dao.findOrderListByChangGuanAndStateStartDateEndDate(ORDER_TYPE_REACH,changdi,start_date,end_date,0,2000000);
            }

        }

        List<String> heads = new ArrayList<>();
        heads.add("编号");
        heads.add("场馆");
        heads.add("片场");
        heads.add("会员名称");
        heads.add("会员电话");
        heads.add("预约日期");
        heads.add("预约时间段");
        heads.add("价格");
        heads.add("状态");
        ByteArrayOutputStream baos = null;
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("报表");
        ExcelUtil.headList(heads, sheet);

        double sumprice = 0;
        double sumprice_xianjin = 0;
        double sumprice_alipay = 0;
        int cell = 1;
        for (Order order : list) {
            HSSFRow headerRow = sheet.createRow(cell);// 行
            int row = 0;
            List cloums = new ArrayList<>();
            cloums.add(order.getOid());//编号
            if(null == order.getChangguan_name()){
                cloums.add("-");//场馆名
            }else{
                cloums.add(order.getChangguan_name());//场馆名
            }

            if(null == order.getPianchang_name()){
                cloums.add("-");//片场
            }else{
                cloums.add(order.getPianchang_name());//片场
            }

            cloums.add(order.getName());//名称
            cloums.add(order.getPhone());//电话
            cloums.add(order.getReserveTime());//日期
            cloums.add(order.getBetTime());//时间段
            cloums.add(order.getPrice() + "");//价格
            if (order.getOpenid() == null) {
                cloums.add("现场到场");
            } else {
                if(order.getType()==7){
                    cloums.add("现金到场");
                }else if(order.getType()==6){
                    cloums.add("支付宝到场");
                }

            }
            sumprice = sumprice + order.getPrice();
            if(order.getType()==7){
                sumprice_xianjin = sumprice_xianjin + order.getPrice();
            }else if(order.getType()==6){
                sumprice_alipay = sumprice_alipay + order.getPrice();
            }

            for (Object aaa : cloums) {
                HSSFCell cell0 = headerRow.createCell(row); // 列
                cell0.setCellValue(aaa.toString());
                row += 1;
            }
            cell += 1;
        }
        cell = cell + 1;
        HSSFRow headerRow = sheet.createRow(cell);// 行
        HSSFCell cell0 = headerRow.createCell(0); // 列
        cell0.setCellValue("总计");

        HSSFCell cell02 = headerRow.createCell(1); // 列
        cell02.setCellValue("订单数量:" + list.size());

        HSSFCell cell3 = headerRow.createCell(2); // 列
        cell3.setCellValue("总销售额" + sumprice);

        HSSFCell cell4 = headerRow.createCell(3); // 列
        cell4.setCellValue("现金销售额" + sumprice_xianjin);

        HSSFCell cell5 = headerRow.createCell(4); // 列
        cell5.setCellValue("支付宝销售额" + sumprice_alipay);

        String fileName="健身报表"+start_date+"-"+(int)(System.currentTimeMillis()/1000)+".xls";
        String path = session.getServletContext().getRealPath(Constants.FILE_PATH);
        String downloadPath=Constants.SERVER_HOST+Constants.FILE_PATH+fileName;
        File file =null;

        // path = ResourceUtils.getURL("classpath:").getPath();

        File FilePath = new File(path);
        if(!FilePath.exists()){
            FilePath.mkdirs();
        }

        // 生成文件
        file = new File(path,fileName);
        try {
            file.createNewFile();
            if(!file.exists()){
                file.createNewFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



        LOG.info("path:"+path+fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);//写出文件
            fos.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {

        }


        // FastDFSClient fastDFSClient = new FastDFSClient();
        // String url = fastDFSClient.uploadFile(baos.toByteArray(), "xls");

        return R.ok(downloadPath);
    }

    @Override
    public List<PeriodTimeClose> findPeriodTimeCloseListByCloseDateLessThan(Date date) {
        List<PeriodTimeClose> list = dao.findPeriodTimeCloseListByCloseDateLessThan(date);
        return list;
    }

    @Override
    public void deletePeriodTimeCloseById(int id) {
        dao.deletePeriodTimeCloseById(id);
    }

}

