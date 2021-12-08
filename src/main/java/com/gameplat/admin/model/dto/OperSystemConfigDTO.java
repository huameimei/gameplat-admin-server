package com.gameplat.admin.model.dto;

import cn.hutool.json.JSONObject;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class OperSystemConfigDTO implements Serializable {

  private JSONObject jsonData;

  private String dictType;
}
