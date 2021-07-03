package com.gameplat.admin.model.dto;

import com.gameplat.common.model.dto.BaseDto;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class PayTypeQueryDTO implements Serializable {

  private Integer isSystemCode;

  private Integer status;

  private Integer onlinePayEnabled;
}
