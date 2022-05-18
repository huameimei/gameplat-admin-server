package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 活动黑名单DTO
 *
 * @author aBen @Description 实体层
 * @date 2020-08-20 11:30:39
 */
@Data
public class ActivityBlacklistDTO implements Serializable {

  private static final long serialVersionUID = -1005615158531421103L;

  @Schema(description = "主键ID")
  private Long id;

  @Schema(description = "活动ID")
  private Long activityId;

  @Schema(description = "限制内容")
  private String limitedContent;

  @Schema(description = "限制类型 1会员账号  2 ip地址")
  private Integer limitedType;

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "IP地址")
  private String ipAddress;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  private String createTime;

  @Schema(description = "删除的ID集合")
  private List<Long> ids;
}
