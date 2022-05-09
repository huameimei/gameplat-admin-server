package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TpMerchantPayTypeVO implements Serializable {

  private Long id;

  private String name;

  private String parameters;

  private String payTypes;

  private String tpInterfaceCode;

  private List<TpPayTypeVO> tpPayTypeVOList;

  private TpInterfaceVO tpInterfaceVO;
}
