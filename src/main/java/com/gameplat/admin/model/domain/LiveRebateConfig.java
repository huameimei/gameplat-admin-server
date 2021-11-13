package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("live_rebate_config")
public class LiveRebateConfig implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  /**
   * 用户层级
   */
  private String userLevel;

  /**
   * 投注额阈值
   */
  private BigDecimal money;

  /**
   * 返点具体配置
   */
  private String json;

  private String expand;
}
