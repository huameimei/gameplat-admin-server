package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springdoc.api.annotations.ParameterObject;

import java.io.Serializable;

/**
 * @author zhuzi
 * @program: open-live-platform
 * @description: 直播流量查询
 * @date 2020-08-31 14:06:06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ParameterObject
public class LiveDomainParamDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "需要查询的直播域名")
  private String domain;

  @Schema(
      description =
          "获取数据起始时间点，最长可查询90天内的数据，格式：yyyy-MM-dd HH:mm:ss 默认本月第一天0点（如：2020-12-01 00:00:00）")
  private String startTime;

  @Schema(description = "结束时间需大于起始时间,格式：yyyy-MM-dd HH:mm:ss 默认当期时间")
  private String endTime;

  @Schema(description = "查询数据的时间粒度，支持300, 3600和86400秒")
  private String interval;

  @Schema(description = "日期范围类型 0 单月 1 上月")
  private String dateType;

  @Schema(description = "zh-CN:中文;en-US:英文")
  private String country;
}
