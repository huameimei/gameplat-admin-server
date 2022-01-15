package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import lombok.Data;

@Data
public class OperGameMemberDayReportDTO implements Serializable {

  private String platformCode;

  @JsonFormat(locale ="zh", timezone ="GMT+8", pattern = "yyyy-MM-dd")
  private String statTime;

}
