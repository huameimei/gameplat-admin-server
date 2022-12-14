package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gameplat.security.SecurityUserHolder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class AgentDomainDTO {

  private Long id;

  /** 协议 */
  private String promoteProtocol;

  /** 域名 */
  private String domain;

  /** 0 公共域名  1 专属域名 */
  private String type;

  /** 备注 */
  private String remark;

  /** 状态 0 关闭  1 开启 */
  private Integer status;

  @TableField(fill = FieldFill.INSERT)
  @Schema(description = "创建者")
  private String createBy = SecurityUserHolder.getCredential().getUsername();

  /** 创建时间 */
  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime = new Date();

  /** 更新者 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  private String updateBy = SecurityUserHolder.getCredential().getUsername();;

  /** 更新时间 */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime = new Date();
}
