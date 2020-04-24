package com.atxca.order.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author 王志鹏
 * @title: OrderVO
 * @projectName atxca
 * @description: TODO
 * @date 2019/5/15 11:16
 */

@Data
@ApiModel(value = "OrderVO对象", description = "微信订单渲染")
public class OrderVO {

    @ApiModelProperty(value = "场馆名称", name = "name;", example = "篮球馆1号")
    private String name;

    @ApiModelProperty(value = "场地名称", name = "vName;", example = "1号片场(篮球01场地)")
    private String vName;

    @ApiModelProperty(value = "预约时间:使用日期", name = "reserveTime;", example = "2019-05-01")
    private String reserveTime;

    @ApiModelProperty(value = "使用时间段", name = "betTime;", example = "2019-05-01")
    private String betTime;

    @ApiModelProperty(value = "用户名", name = "userName;", example = "小王")
    private String userName;

    @ApiModelProperty(value = "使用时间段", name = "userPhone;", example = "2019-05-01")
    private String userPhone;

    @ApiModelProperty(value = "订单号", name = "orderId;", example = "21312dsaf3123")
    private String orderId;

    @ApiModelProperty(value = "状态:1取消,2待审批,3待付款,4预约成功", name = "type", example = "1")
    private int type;
}
