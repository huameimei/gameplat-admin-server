package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 新增活动黑名单DTO
 *
 * @author aBen @Description 实体层
 * @date 2020-08-20 11:30:39
 */
@Data
public class ActivityBlacklistAddDTO implements Serializable {

  private static final long serialVersionUID = -1005615158531421103L;

  @ApiModelProperty(value = "活动ID")
  private Long activityId;

  @ApiModelProperty(value = "限制内容")
  private String limitedContent;

  @ApiModelProperty(value = "限制类型 1会员账号  2 ip地址")
  private Integer limitedType;
}
