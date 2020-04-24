package com.atxca.core.Enums;

/**
 * @author 王志鹏
 * @title: SysEnum
 * @projectName gymnasium
 * @description: 系统常量
 * @date 2019/4/12 9:02
 */

public enum SysEnum {

    STATUS_NORMAL(0, "可以预约"),
    STATUS_CHECK_IN(1, "待审核"),
    STATUS_RESERVE_IN(2, "已预约"),
    STATUS_DISABLE_IN(3, "审核拒绝"),
    STATUS_USING_IN(4, "正在被使用"),
    ;


    //空置可被预约：0白色
    //已被外部预约待人工审核：1红色
    //外部预约已审核通过，被预约状态：2绿色
    //已被内部关闭：3灰色
    //正在被使用：4蓝色


    private int val;

    private String remarks;

    SysEnum(int val, String remarks) {
        this.val = val;
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public int getVal() {

        return val;
    }
}
