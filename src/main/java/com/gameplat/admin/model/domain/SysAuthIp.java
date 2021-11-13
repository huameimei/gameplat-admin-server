package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.dozer.Mapping;

import java.util.Date;

/**
 * ip白名单
 *
 * @author three
 */
@Data
@TableName("sys_auth_ip")
public class SysAuthIp {

  @TableId(type = IdType.AUTO)
  private Long id;

  /** IP白名单 */
  @Mapping(value = "ip")
  private String allowIp;

  /** 类型 0-后端， 1-前端 多个逗号隔开 */
  private String ipType;

  /** 创建者 */
  @TableField(fill = FieldFill.INSERT)
  private String createBy;

  /** 创建时间 */
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

  /** 更新人 */
  @TableField(fill = FieldFill.UPDATE)
  private String updateBy;

  /** 更新时间 */
  @TableField(fill = FieldFill.UPDATE)
  private Date updateTime;

  /** 备注 */
  private String remark;
}
