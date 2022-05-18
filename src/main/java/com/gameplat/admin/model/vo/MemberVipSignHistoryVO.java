package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description : VIP会员签到历史记录出参 @Author : lily @Date : 2021/12/07
 */
@Data
public class MemberVipSignHistoryVO implements Serializable {
  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "会员ID")
  private Long userId;

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "签到时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date signTime;

  @Schema(description = "签到IP")
  private String signIp;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;
}
