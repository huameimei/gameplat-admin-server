package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description : VIP会员签到历史记录出参 @Author : lily @Date : 2021/12/07
 */
@Data
public class MemberVipSignHistoryVO implements Serializable {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty("主键")
  private Long id;

  @ApiModelProperty("会员ID")
  private Long userId;

  @ApiModelProperty("会员账号")
  private String userName;

  @ApiModelProperty(value = "签到时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date signTime;

  @ApiModelProperty(value = "签到IP")
  private String signIp;

  @ApiModelProperty(value = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @ApiModelProperty(value = "更新时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;
}
