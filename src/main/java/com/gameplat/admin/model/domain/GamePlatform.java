package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("game_platform")
public class GamePlatform implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  private String code;

  private String name;

  private Integer transfer;

  private String sort;

  private Date createTime;

  private Date updateTime;
}
