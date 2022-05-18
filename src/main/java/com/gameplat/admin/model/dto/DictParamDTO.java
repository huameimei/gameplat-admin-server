package com.gameplat.admin.model.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class DictParamDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private JSONObject jsonData;

  private String dictType;

  private Integer gameBlacklistId;

  private String gameBlacklistHint;
}
