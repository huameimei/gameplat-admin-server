package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiveGameResultVO {

  @JsonProperty
  private boolean redirect;
  private String data;

  public LiveGameResultVO() {
    this(Boolean.TRUE);
  }

  public LiveGameResultVO(boolean redirect) {
    this.redirect = redirect;
  }

  public LiveGameResultVO(String data) {
    this(Boolean.TRUE,data);
  }

  public LiveGameResultVO(boolean redirect,String data) {
    this.redirect = redirect;
    this.data = data;
  }

  public boolean redirect() {
    return redirect;
  }

  public void setRedirect(boolean redirect) {
    this.redirect = redirect;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
