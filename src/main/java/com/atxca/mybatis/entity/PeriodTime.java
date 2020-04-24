package com.atxca.mybatis.entity;

public class PeriodTime{


  private int pid;


  //@ApiModelProperty(value = "外键", name = "vid", example = "1")
  private int vid;


 // @ApiModelProperty(value = "时段", name = "betTime", example = "10:00-11:00")
  private String betTime;


 // @ApiModelProperty(value = "周一到周五价格", name = "price", example = "19")
  private double price;


  private double weekPrice;


  //@ApiModelProperty(value = "1空置可预约,2待审核,3待付款(已预约),4内部关闭,5已使用", name = "type", example = "0")
  private int type;

  private String orderId;

  private int vhouse_id;

  private int sort;



  public int getPid() {
    return pid;
  }

  public void setPid(int pid) {
    this.pid = pid;
  }

  public int getVid() {
    return vid;
  }

  public void setVid(int vid) {
    this.vid = vid;
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

  public double getWeekPrice() {
    return weekPrice;
  }

  public void setWeekPrice(double weekPrice) {
    this.weekPrice = weekPrice;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

    public int getVhouse_id() {
        return vhouse_id;
    }

    public void setVhouse_id(int vhouse_id) {
        this.vhouse_id = vhouse_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
