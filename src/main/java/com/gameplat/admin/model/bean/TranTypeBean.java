package com.gameplat.admin.model.bean;

public class TranTypeBean {
  private int type;
  private String desc;
  private int reportType;

  public TranTypeBean() {}

  public TranTypeBean(int type, String desc, int reportType) {
    this.type = type;
    this.desc = desc;
    this.reportType = reportType;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }

  public int getReportType() {
    return reportType;
  }

  public void setReportType(int reportType) {
    this.reportType = reportType;
  }
}
