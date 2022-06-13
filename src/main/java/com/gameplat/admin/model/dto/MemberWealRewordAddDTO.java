package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 新增福利记录入参
 * @date 2021/11/28
 */
@Data
public class MemberWealRewordAddDTO implements Serializable {

  @Schema(description = "用户ID")
  private Long userId;

  @Schema(description = "用户账号")
  private String userName;

  @Schema(description = "状态： 0：待审核   1：未领取  2：已完成  3:已失效")
  private Integer status;

  @Schema(description = "会员类型")
  private String userType;

  @Schema(description = "上级账号")
  private String parentName;

  @Schema(description = "上级id")
  private Long parentId;

  @Schema(description = "代理路由")
  private String agentPath;

  @Schema(description = "老等级")
  private Integer oldLevel;

  @Schema(description = "当前等级")
  private Integer currentLevel;

  @Schema(description = "派发金额")
  private BigDecimal rewordAmount;

  @Schema(description = "提现打码量")
  private BigDecimal withdrawDml;

  @Schema(description = "类型：0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金  4：每月红包")
  private Integer type;

  @Schema(description = "领取时间")
  private Date drawTime;

  @Schema(description = "失效时间")
  private Date invalidTime;

  @Schema(description = "流水号")
  private String serialNumber;

  @Schema(description = "活动标题")
  private String activityTitle;

  @Schema(description = "备注")
  private String remark;
}
