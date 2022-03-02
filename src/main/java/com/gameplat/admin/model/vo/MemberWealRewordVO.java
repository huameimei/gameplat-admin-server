package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty("主键")
  private Long id;

  @ApiModelProperty("用户账号")
  @Excel(name = "用户账号", height = 20, width = 30, isImportField = "true_st")
  private String userName;

  @ApiModelProperty("当前等级")
  @Excel(name = "当前等级", suffix = "VIP", height = 20, width = 30, isImportField = "true_st")
  private Integer currentLevel;

  @ApiModelProperty("派发金额")
  @Excel(name = "派发金额", height = 20, width = 30, isImportField = "true_st")
  private BigDecimal rewordAmount;

  @ApiModelProperty("状态： 0：待审核   1：未领取  2：已完成  3:已失效")
  @Excel(
      name = "状态",
      replace = {"待审核_0", "未领取_1", "已完成_2", "已失效_3"},
      isImportField = "true_st")
  private Integer status;

  @ApiModelProperty("类型：0 升级奖励  1：周俸禄  2：月俸禄  3：生日礼金  4：每月红包")
  @Excel(
      name = "类型",
      replace = {"升级奖励_0", "周俸禄_1", "月俸禄_2", "生日礼金_3", "每月红包_4"},
      isImportField = "true_st")
  private Integer type;

  @ApiModelProperty(value = "创建时间")
  @Excel(
      name = "创建时间",
      databaseFormat = "yyyyMMddHHmmss",
      format = "yyyy-MM-dd HH:mm:ss",
      isImportField = "true_st",
      width = 20)
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;
}
