package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@TableName("game_rebate_periods")
public class GameRebatePeriod implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String name;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date beginDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date endDate;

  private String blackAccounts;

  private String blackLevels;

  private Integer status;

  @TableField(fill = FieldFill.INSERT)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date statTime;
}
