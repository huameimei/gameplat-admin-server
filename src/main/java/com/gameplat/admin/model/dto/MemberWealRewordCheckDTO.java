package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description : 审核升级奖励入参 @Author : lily @Date : 2021/11/23
 */
@Data
public class MemberWealRewordCheckDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  private Long id;

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

  @Schema(description = "备注")
  private String remark;

  @Schema(description = "开始时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date startTime;

  @Schema(description = "结束时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date endTime;
}
