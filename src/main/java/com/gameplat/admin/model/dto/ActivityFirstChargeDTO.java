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
 * 每日首充表
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityFirstChargeDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "页面大小")
  private Integer pageSize;

  @Schema(description = "第几页")
  private Integer pageNum;

  @Schema(description = "主键")
  private Long[] ids;

  @Schema(description = "主键")
  @Mapping(value = "chargeId")
  private Long id;

  @Schema(description = "开始时间")
  private Date beginTime;

  @Schema(description = "结束时间")
  private Date endTime;

  @Schema(description = "首充类型")
  private Integer chargeType;

  @Schema(description = "每日首充条件")
  private String chargeTerm;

  @Schema(description = "首充标题")
  private String chargeTitle;

  @Schema(description = "首充展示位置")
  private String chargeDisplay;

  @Schema(description = "状态 0下线 1上线")
  private Integer status;

  @Schema(description = "被删除的优惠列表")
  private Long[] delDiscountIdList;

  @Schema(description = "优惠列表")
  private List<ActivityChargeDiscountDTO> discountlist;

  public void setBeginTime(String beginTime) {
    this.beginTime = DateUtil.strToDate(beginTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }

  public void setEndTime(String endTime) {
    this.endTime = DateUtil.strToDate(endTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
  }
}
