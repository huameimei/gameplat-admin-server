package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@TableName("game_blacklist")
public class GameBlacklist implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String target;

  /**
   * 单个会员账号，会员层级
   */
  private Integer targetType;

  /**
   * 具体业务类型
   */
  private String blackType;

  /**
   * 真人游戏编码，多个逗号拼接
   */
  private String liveCategory;

  private String remarks;

  private Date createTime;

  private String createBy;

  private Date updateTime;

  private String updateBy;
}
