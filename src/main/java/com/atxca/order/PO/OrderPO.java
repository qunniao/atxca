package com.atxca.order.PO;

import com.atxca.sportshall.PO.VenuePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author 王志鹏
 * @title: OrderPO
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 15:06
 */

@Data
@Entity
@Table(name = "zc_order", catalog = "atxca")
@ApiModel(value = "OrderPO", description = "预约订单")
public class OrderPO implements Serializable {

    @Id
    @Column(name = "oid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "订单主键", name = "oid", example = "1")
    private int oid;

    @Column(name = "pid")
    @ApiModelProperty(value = "场地时间段主键", name = "pid", example = "1")
    private int pid;

    @Column(name = "vid")
    private int vid;

    @Column(name = "openid")
    @ApiModelProperty(value = "openid", name = "openid", example = "fasa897hd48s4fhadfdfa")
    private String openid;

    @Column(name = "orderId")
    @ApiModelProperty(value = "订单编号", name = "orderId", example = "1")
    private String orderId;//订单编号

    @Column(name = "name")
    @ApiModelProperty(value = "联系人姓名", name = "name", example = "1")
    private String name;//联系人姓名

    @Column(name = "phone")
    @ApiModelProperty(value = "联系人电话", name = "phone", example = "1")
    private String phone;//联系人电话

    @Column(name = "betTime")
    @ApiModelProperty(value = "预约时间段", name = "betTime", example = "10:00-11:00")
    private String betTime;

    @Column(name = "reserveTime")
    @ApiModelProperty(value = "预约日期(天)", name = "reserveTime", example = "2019-05-18")
    private String reserveTime;

    @Column(name = "price")
    @ApiModelProperty(value = "价格", name = "price", example = "1")
    private double price;

    @Column(name = "type")
    @ApiModelProperty(value = "订单状态 1,预约待审核,2预约成功,3预约到场,4预约失败,5预约未到", name = "type", example = "1")//0为系统保留
    private int type;//订单状态

    @Column(name = "remakes")
    @ApiModelProperty(value = "备注", name = "remakes", example = "1")
    private String remakes;//备注

    @Column(name = "createTime")
    @ApiModelProperty(value = "创建时间", name = "createTime", example = "1")
    private Timestamp createTime;//创建时间

}
