package com.atxca.mybatis.entity;

import java.util.List;

public class YuYuePO {
    private String date;
    private String dateName;
    private String week;
    private String weekName;
    private Integer dateCount;
    private List<PianChangPO> pianChangList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getDateCount() {
        return dateCount;
    }

    public void setDateCount(Integer dateCount) {
        this.dateCount = dateCount;
    }

    public List<PianChangPO> getPianChangList() {
        return pianChangList;
    }

    public void setPianChangList(List<PianChangPO> pianChangList) {
        this.pianChangList = pianChangList;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }
}
