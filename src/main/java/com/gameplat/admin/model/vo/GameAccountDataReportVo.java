package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author kb @Date 2022/3/5 14:07 @Version 1.0
 */
@Data
public class GameAccountDataReportVo implements Serializable {

  @ApiModelProperty(value = "登录人数")
  private int regNum;

  @ApiModelProperty(value = "注册人数")
  private int logNum;

  @ApiModelProperty(value = "全部余额")
  private BigDecimal goodMoney;

  @ApiModelProperty(value = "过期时间")
  private String expiredTime;

  @ApiModelProperty(value = " 可用额度")
  private BigDecimal quota;

  @ApiModelProperty(value = " 全部会员")
  private int accountNum;

  private List<AccountReportVo> list = new ArrayList<>();
}
