package com.atxca.mybatis.entity;

public class TimePO {
    private int pid;
    private String betTime;
    private double price;
    private int isYuYue;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getBetTime() {
        return betTime;
    }

    public void setBetTime(String betTime) {
        this.betTime = betTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIsYuYue() {
        return isYuYue;
    }

    public void setIsYuYue(int isYuYue) {
        this.isYuYue = isYuYue;
    }
}
