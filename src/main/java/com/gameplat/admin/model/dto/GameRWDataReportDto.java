package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author kb @Date 2022/3/2 21:38 @Version 1.0
 */
@Data
public class GameRWDataReportDto implements Serializable {

  /** 账号 */
  private String account;

  /** 代理账号 */
  private String superAccount;

  /** 是否直属会员 （1 是 0 否） */
  private int flag;

  /** 开始日期 */
  private String startTime;

  /** 结束日期 */
  private String endTime;

  /**
   * 充值提现  首次二次  1 充值首次  2 充值二次  3 提现首次  4 提现二次
   */
  private String type;

  @Schema(description = "分页大小")
  private Integer current = 10;

  @Schema(description = "起始下标")
  private Integer size = 1;

  private Integer getCurrent() {
    return (this.current - 1) * size;
  }

}
