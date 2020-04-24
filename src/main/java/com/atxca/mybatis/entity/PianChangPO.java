package com.atxca.mybatis.entity;

import com.atxca.data.PO.TypePO;
import com.atxca.sportshall.PO.PeriodTimePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author 王志鹏
 * @title: VenuePO
 * @projectName atxca
 * @description: TODO
 * @date 2019/4/19 10:16
 */

public class PianChangPO{

    private int vid;

    private String vName;

    private Integer pianchangCount;

    private List<TimePO> timePOList;

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public Integer getPianchangCount() {
        return pianchangCount;
    }

    public void setPianchangCount(Integer pianchangCount) {
        this.pianchangCount = pianchangCount;
    }

    public List<TimePO> getTimePOList() {
        return timePOList;
    }

    public void setTimePOList(List<TimePO> timePOList) {
        this.timePOList = timePOList;
    }
}
