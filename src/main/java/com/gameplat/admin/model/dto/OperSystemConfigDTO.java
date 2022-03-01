package com.gameplat.admin.model.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class OperSystemConfigDTO implements Serializable {

  private JSONObject jsonData;

  private String dictType;
}
