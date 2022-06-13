package com.gameplat.admin.model.dto;

import com.gameplat.base.common.util.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lyq @Description 转盘DTO
 * @date 2020年5月28日 上午11:37:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityTurntableDTO implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  @Schema(description = "页面大小")
  private Integer pageSize;

  @Schema(description = "第几页")
  private Integer pageNum;

  @Schema(description = "主键")
  private Long[] ids;

  @Schema(description = "主键")
  @Mapping(value = "turntableId")
  private Long turntableId;

  @Schema(description = "开始时间")
  private Date beginTime;

  @Schema(description = "结束时间")
  private Date endTime;

  @Schema(description = "类型（数据字典：game游戏，live：直播）")
  private String type;

  @Schema(description = "展示位置")
  private String display;

  @Schema(description = "转1次消耗")
  private Integer turnOne;

  @Schema(description = "转10此消耗")
  private Integer turnTen;

  @Schema(description = "转1次幸运值")
  private Integer turnOneLucky;

  @Schema(description = "转10次幸运值")
  private Integer turnTenLucky;

  @Schema(description = "总幸运值")
  private Integer totalLucky;

  @Schema(description = "幸运值满赠送")
  private Long luckyGive;

  @Schema(description = "状态 0下线 1上线")
  private Integer status;

  @Schema(description = "转盘标题")
  private String turnTitle;

  @Schema(description = "红包时间(周一到周日)")
  private String weekTime;

  @Schema(description = "被删除的奖品列表")
  private Long[] deleteIdList;

  @Schema(description = "奖品列表")
  private List<ActivityPrizeDTO> activityPrize;

  public void setBeginTime(String beginTime) {
    this.beginTime = DateUtil.strToDate(beginTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }

  public void setEndTime(String endTime) {
    this.endTime = DateUtil.strToDate(endTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }
}
