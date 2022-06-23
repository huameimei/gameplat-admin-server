package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description : VIP福利记录入参 @Author : lily @Date : 2021/11/23
 */
@Data
public class MemberWealRewordDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "用户ID")
  private Long userId;

  @Schema(description = "用户账号")
  private String userName;

  @Schema(description = "状态： 0：待审核   1：未领取  2：已完成  3:已失效")
  private Integer status;

  @Schema(description = "流水号")
  private String serialNumber;

  @Schema(description = "类型：0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金  4：每月红包")
  private Integer type;

  @Schema(description = "开始时间")
  private String startTime;

  @Schema(description = "结束时间")
  private String endTime;

  @Schema(description = "vip等级")
  private Integer vipLevel;

  @Schema(description = "代理账号")
  private String superAccount;

  @Schema(description = "是否只查询直属下级 0否1是")
  private String flag;

  @Schema(description = "用户账号集合")
  private List<String> userNameList;
}
