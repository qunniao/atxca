package com.atxca.sportshall.PO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author 王志鹏
 * @title: VenueOrderPO
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/27 9:49
 */
@Data
@Entity
@Table(name = "venue_order", catalog = "atxca")
@ApiModel(value = "VenueOrderPO", description = "预约订单")
public class VenueOrderPO implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "订单主键", name = "id", example = "id")
    private int id;

    @Column(name = "pid")
    @ApiModelProperty(value = "periodtime主键", name = "pid", example = "1")
    private int pid;

    @Column(name = "vid")
    @ApiModelProperty(value = "venue主键", name = "vid", example = "1")
    private int vid;

    @Column(name = "betTime")
    @ApiModelProperty(value = "时间段", name = "betTime", example = "1")
    private String betTime;

    @Column(name = "reserveTime")
    @ApiModelProperty(value = "指定日期", name = "reserveTime", example = "1")
    private String reserveTime;

    @Column(name = "type")
    @ApiModelProperty(value = "类型", name = "type", example = "1")
    private int type;
}
