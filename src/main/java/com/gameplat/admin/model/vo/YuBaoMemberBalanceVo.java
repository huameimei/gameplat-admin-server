package com.gameplat.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author kb
 * @Date 2022/7/6 23:09
 * @Version 1.0
 */
@Data
public class YuBaoMemberBalanceVo implements Serializable {

  @Schema(name = "账号")
  private String account;

  @Schema(name = "真实姓名")
  private String realName;

  @Schema(name = "余额")
  private BigDecimal balance;

  @Schema(name = "返点")
  private BigDecimal rebate;

  @Schema(name = "层级")
  private String levelName;

  @Schema(name = "上级代理")
  private String parentName;

  @Schema(name = "创建时间")
  private String createTime;

  @Schema(name = "状态")
  private Integer state;

  @Schema(name = "余额宝余额")
  private BigDecimal yuBaoBalance;

  @Schema(name = "是否限制投注")
  private String limitLotFlag;

}
