package com.atxca.mybatis.entity;

public class PeriodTimeClose {
    private int id;
    private int pid;
    private String close_time;
    private String close_date;
    private int type;
    private int venues_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getVenues_id() {
        return venues_id;
    }

    public void setVenues_id(int venues_id) {
        this.venues_id = venues_id;
    }

    public String getClose_date() {
        return close_date;
    }

    public void setClose_date(String close_date) {
        this.close_date = close_date;
    }
}
