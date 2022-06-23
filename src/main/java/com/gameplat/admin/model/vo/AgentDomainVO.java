package com.gameplat.admin.model.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class AgentDomainVO implements Serializable {

  private Long id;

  /** 协议 */
  @Excel(name = "协议", width = 20, isImportField = "true_st")
  private String promoteProtocol;

  /** 域名 */
  @Excel(name = "代理域名", width = 20, isImportField = "true_st")
  private String domain;

  /** 0 公共域名  1 专属域名 */
  @Excel(name = "域名类型", width = 40, isImportField = "true_st")
  private String type;

  /** 备注 */
  @Excel(name = "备注", width = 50, isImportField = "true_st")
  private String remark;

  /** 状态 0 关闭  1 开启 */
  @Excel(name = "状态", width = 20, isImportField = "true_st")
  private Integer status;

  @TableField(fill = FieldFill.INSERT)
  @Schema(description = "创建者")
  @Excel(name = "创建者", width = 20, isImportField = "true_st")
  private String createBy;

  /** 创建时间 */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Excel(name = "创建时间", width = 20, isImportField = "true_st")
  private Date createTime;

  /** 更新者 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @Excel(name = "更新者", width = 20, isImportField = "true_st")
  private String updateBy;

  /** 更新时间 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @Excel(name = "更新时间", width = 20, isImportField = "true_st")
  private Date updateTime;
}
