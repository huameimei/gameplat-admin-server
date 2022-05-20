package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动黑名单VO
 *
 * @author aBen @Description 实体层
 * @date 2020-08-20 11:30:39
 */
@Data
public class ActivityBlacklistVO implements Serializable {

  private static final long serialVersionUID = 7535872776555957753L;

  @Schema(description = "主键ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long id;

  @Schema(description = "活动ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long activityId;

  @Schema(description = "限制内容")
  private String limitedContent;

  @Schema(description = "限制类型 1会员账号  2 ip地址")
  private Integer limitedType;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;
}
