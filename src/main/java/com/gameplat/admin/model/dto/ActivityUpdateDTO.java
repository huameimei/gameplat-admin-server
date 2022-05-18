package com.gameplat.admin.model.dto;

import com.gameplat.base.common.util.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.dozer.Mapping;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新活动DTO
 *
 * @author 沙漠
 * @date 2020年5月28日
 */
@Data
public class ActivityUpdateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "ids")
  private Long[] ids;

  @Schema(description = "主键")
  @Mapping(value = "activityId")
  private Long id;

  @Schema(description = "活动名称")
  private String activityName;

  @Schema(description = "是否参与红包雨 0否 1是")
  private Integer isPackage;

  @Schema(description = "是否参与每日首充 0否 1是")
  private Integer isCharge;

  @Schema(description = "是否参与转盘 0否 1是")
  private Integer isTurntable;

  @Schema(description = "开始时间")
  private Date beginTime;

  @Schema(description = "结束时间")
  private Date endTime;

  @Schema(description = "活动类型")
  private Integer activityType;

  @Schema(description = "红包雨")
  private ActivityRedPacketDTO memberRedPacketDTO;

  @Schema(description = "每日首充")
  private ActivityFirstChargeDTO memberFirstChargeDTO;

  @Schema(description = "转盘")
  private ActivityTurntableDTO memberTurntableDTO;

  public void setBeginTime(String beginTime) {
    this.beginTime = DateUtil.strToDate(beginTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }

  public void setEndTime(String endTime) {
    this.endTime = DateUtil.strToDate(endTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }
}
