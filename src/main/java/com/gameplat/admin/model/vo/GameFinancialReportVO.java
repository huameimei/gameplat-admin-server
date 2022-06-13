package com.gameplat.admin.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author aBen
 * @date 2022/3/6 17:28
 * @desc 游戏财务报表
 */
@Data
public class GameFinancialReportVO implements Serializable {

  @Schema(description = "主键ID")
  private Long id;

  @Schema(description = "统计日期（年月）")
  private String statisticsTime;

  @Schema(description = "租户标识")
  private String customerCode;

  @Schema(description = "游戏大类编码")
  private String gameType;

  @Schema(description = "游戏大类名字")
  private String gameTypeName;

  @Schema(description = "游戏大类Id")
  private Integer gameTypeId;

  @Schema(description = "游戏平台编码")
  private String platformCode;

  @Schema(description = "游戏平台名字")
  private String platformName;

  @Schema(description = "一级游戏编码")
  private String gameKind;

  @Schema(description = "一级游戏名称")
  private String gameName;

  @Schema(description = "有效投注额")
  private BigDecimal validAmount;

  @Schema(description = "输赢金额")
  private BigDecimal winAmount;

  @Schema(description = "上月输赢金额")
  private BigDecimal lastWinAmount = BigDecimal.ZERO;

  @Schema(description = "累计输赢金额")
  private BigDecimal accumulateWinAmount;

  @Schema(description = "统计开始时间")
  private String startTime;

  @Schema(description = "统计结束时间")
  private String endTime;

  @Schema(description = "创建时间")
  @JSONField(format = "unixtime")
  private Date createTime;

  @Schema(description = "创建人")
  private String createBy;
}
