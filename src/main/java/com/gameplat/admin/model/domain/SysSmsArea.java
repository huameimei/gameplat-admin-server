package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class SysSmsArea implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;
  /**
   * 编码
   */
  private String code;
  /**
   * 国家/地区
   */
  private String name;

  /**
   * 创建人
   */
  private String createBy;

  /**
   * 创建时间
   */
  private Date createTime;

  /**
   * 更新人
   */
  private String updateBy;

  /**
   * 更新时间
   */
  private Date updateTime;


  /**
   * 状态 0 禁用 1 启用
   */
  private String status;

}
