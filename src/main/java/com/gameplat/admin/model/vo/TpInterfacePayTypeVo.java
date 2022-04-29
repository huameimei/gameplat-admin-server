package com.gameplat.admin.model.vo;

import com.gameplat.model.entity.pay.TpPayType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TpInterfacePayTypeVo implements Serializable {

  private Long id;

  private String name;

  private String code;

  private Integer parameterType;

  private Integer status;

  private String parameters;

  private String orderQueryVersion;

  private String orderQueryMethod;

  private String orderQueryUrl;

  private Integer orderQueryEnabled;

  private String ipFlg;

  private String ipWhiteList;

  private List<TpPayType> tpPayTypeList;
}
