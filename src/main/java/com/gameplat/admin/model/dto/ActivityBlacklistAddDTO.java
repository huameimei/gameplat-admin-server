package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

  @NotNull(message = "活动ID不能为空")
  @Min(value = 1, message = "活动ID必须大于0")
  @Schema(description = "活动ID")
  private Long activityId;

  @NotBlank(message = "限制内容不能为空")
  @Schema(description = "限制内容")
  private String limitedContent;

  @NotNull(message = "限制类型不能为空")
  @Min(value = 1, message = "限制类型必须大于0")
  @Schema(description = "限制类型 1会员账号  2 ip地址")
  private Integer limitedType;
}
