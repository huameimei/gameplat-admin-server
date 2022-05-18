package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description VIP会员签到汇总出参
 * @date 2021/11/24
 */
@Data
public class MemberVipSignStatisVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "主键")
  private Long userId;

  @Schema(description = "会员账号")
  @Excel(name = "会员账号", width = 20, isImportField = "true_st")
  private String userName;

  @Schema(description = "签到次数")
  @Excel(name = "总签到次数", width = 12, isImportField = "true_st")
  private Integer signCount;

  @Schema(description = "最后签到时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  @Excel(
      name = "最后签到时间",
      exportFormat = "yyyy-MM-dd HH:mm:ss",
      format = "yyyy-MM-dd HH:mm:ss",
      isImportField = "true_st",
      width = 22)
  private Date updateTime;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  @Excel(
      name = "创建时间",
      exportFormat = "yyyy-MM-dd HH:mm:ss",
      format = "yyyy-MM-dd HH:mm:ss",
      isImportField = "true_st",
      width = 22)
  private Date createTime;
}
