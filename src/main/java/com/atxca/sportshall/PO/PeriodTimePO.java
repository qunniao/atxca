package com.atxca.sportshall.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author 王志鹏
 * @title: PeriodTime
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 10:30
 */

@Data
@Entity
@Table(name = "periodtime", catalog = "atxca")
@ApiModel(value = "PeriodTimePO", description = "场地详细")
public class PeriodTimePO implements Serializable {

    @Id
    @Column(name = "pid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键", name = "pid", example = "1")
    private int pid;


    @Column(name = "betTime")
    @ApiModelProperty(value = "时段", name = "betTime", example = "10:00-11:00")
    private String betTime;

    @Column(name = "price")
    @ApiModelProperty(value = "周一到周五价格", name = "price", example = "19")
    private double price;

    @Column(name = "weekPrice")
    @ApiModelProperty(value = "周六日价格", name = "weekPrice", example = "199")
    private double weekPrice;

    @Transient
    private String orderId;

    public static void main(String[] args) {

//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar calendar = Calendar.getInstance();
//        Date theDate = calendar.getTime();
//        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
//        gcLast.setTime(theDate);
//        //设置为第一天
//        gcLast.set(Calendar.DAY_OF_MONTH, 1);
//        String day_first = sf.format(gcLast.getTime());
//        //打印本月第一天
//        System.out.println(day_first);

        Calendar calendar = Calendar.getInstance();
        //设置日期为本月最大日期
        calendar.set(Calendar.DATE, calendar.getActualMaximum(calendar.DATE));
        //设置日期格式
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String ss = sf.format(calendar.getTime());
        System.out.println(ss + " 23:59:59");
    }
}
