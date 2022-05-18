package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description : 查询福利奖励中心出参 @Author : lily @Date : 2021/11/23
 */
@Data
public class MemberWealRewordVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "用户账号")
  @Excel(name = "用户账号", height = 20, width = 30, isImportField = "true_st")
  private String userName;

  @Schema(description = "当前等级")
  @Excel(
      name = "当前等级",
      height = 20,
      width = 30,
      isImportField = "true_st",
      replace = {
        "VIP0_0", "VIP1_1", "VIP2_2", "VIP3_3", "VIP4_4", "VIP5_5", "VIP6_6",
        "VIP7_7", "VIP8_8", "VIP9_9", "VIP10_10", "VIP11_11", "VIP12_12", "VIP13_13",
        "VIP14_14", "VIP15_15", "VIP16_16", "VIP17_17", "VIP18_18", "VIP19_19", "VIP20_20",
        "VIP21_21", "VIP22_22", "VIP23_23", "VIP24_24", "VIP25_25", "VIP26_26", "VIP27_27",
        "VIP28_28", "VIP29_29", "VIP30_30", "VIP31_31", "VIP32_32", "VIP33_33", "VIP34_34"
      })
  private Integer currentLevel;

  @Schema(description = "派发金额")
  @Excel(name = "派发金额", height = 20, width = 30, isImportField = "true_st")
  private BigDecimal rewordAmount;

  @Schema(description = "状态： 0：待审核   1：未领取  2：已完成  3:已失效")
  @Excel(
      name = "状态",
      replace = {"待审核_0", "未领取_1", "已完成_2", "已失效_3"},
      isImportField = "true_st")
  private Integer status;

  @Schema(description = "类型：0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金  4：每月红包")
  @Excel(
      name = "类型",
      replace = {"升级奖励_0", "周俸禄_1", "月俸禄_2", "生日礼金_3", "每月红包_4"},
      isImportField = "true_st")
  private Integer type;

  @Schema(description = "创建时间")
  @Excel(
      name = "创建时间",
      databaseFormat = "yyyyMMddHHmmss",
      format = "yyyy-MM-dd HH:mm:ss",
      isImportField = "true_st",
      width = 20)
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "备注")
  private String remark;
}
