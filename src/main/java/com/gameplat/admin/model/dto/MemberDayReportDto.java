package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb @Date 2022/2/8 18:34 @Version 1.0
 */
@Data
public class MemberDayReportDto implements Serializable {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(description = "开始日期")
  private String startTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(description = "结束日期")
  private String endTime;

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "是否首充 N 否 Y 是")
  private String isFristCharge;

  @Schema(description = "是否首提 N 否 Y 是")
  private String isFristWithdraw;

  private String isNFristCharge = "N";
  private String isYFristCharge = "Y";
  private String isNFristWithdraw = "N";
  private String isYFristWithdraw = "Y";
}
