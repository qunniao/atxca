package com.atxca.mybatis.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class Order {
    //@ApiModelProperty(value = "订单主键", name = "oid", example = "1")
    private int oid;


    //@ApiModelProperty(value = "场地时间段主键", name = "pid", example = "1")
    private int pid;

   // @Column(name = "vid")
    private int vid;

    //@ApiModelProperty(value = "openid", name = "openid", example = "fasa897hd48s4fhadfdfa")
    private String openid;

    //@ApiModelProperty(value = "订单编号", name = "orderId", example = "1")
    private String orderId;//订单编号

    //@ApiModelProperty(value = "联系人姓名", name = "name", example = "1")
    private String name;//联系人姓名

    //@ApiModelProperty(value = "联系人电话", name = "phone", example = "1")
    private String phone;//联系人电话

    //@ApiModelProperty(value = "预约时间段", name = "betTime", example = "10:00-11:00")
    private String betTime;

   //@ApiModelProperty(value = "预约日期(天)", name = "reserveTime", example = "2019-05-18")
    private String reserveTime;

    //@ApiModelProperty(value = "价格", name = "price", example = "1")
    private double price;

   //@ApiModelProperty(value = "订单状态 1,预约待审核,2预约成功,3预约到场,4预约失败,5预约未到，6支付宝到场", name = "type", example = "1")//0为系统保留
    private int type;//订单状态


    //@ApiModelProperty(value = "备注", name = "remakes", example = "1")
    private String remakes;//备注


   // @ApiModelProperty(value = "创建时间", name = "createTime", example = "1")
    private Timestamp createTime;//创建时间

    private String changguan_name;
    private String pianchang_name;



}
