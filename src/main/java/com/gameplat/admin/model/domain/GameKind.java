package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@TableName("game_kind")
public class GameKind implements Serializable {

  @TableId(type = IdType.AUTO)
  private Long id;

  /** 平台编码 */
  private String code;

  /** 平台名称 */
  private String name;

  /** 是否开放试玩(0：否；1:是) */
  private Integer demoEnable;

  /**
   * 平台编码
   */
  private String platformCode;

  /**
   * 是否开放(0：否；1:是)
   */
  private Integer enable;

  /**
   * 运营状态(0已下架、1下架中、2运营中)
   */
  private Integer status;

  /**
   * 是否热门(0：否；1:是)
   */
  private Integer hot;

  /**
   * 是否有下级游戏(0：否；1:是)
   */
  private Integer subLevel;

  /**
   * 排序
   */
  private Integer sort;

  /**
   * PC图片地址
   */
  private String pcPicUrl;

  /**
   * APP图片地址
   */
  private String appPicUrl;


  /**
   * 游戏类型
   */
  private String gameType;

  /**
   * 返水最高比例
   */
  private Integer rebateRate;

  /**
   * 是否外跳(0：否；1:是)
   */
  private Integer isJump;

  /**
   * 是否竖屏(0：否；1:是)
   */
  private Integer isVertical;

  /**
   * 维护开始时间
   */
  @TableField(updateStrategy = FieldStrategy.IGNORED)
  private Date maintenanceTimeStart;

  /**
   * 维护结束时间
   */
  @TableField(updateStrategy = FieldStrategy.IGNORED)
  private Date maintenanceTimeEnd;


  /**
   * 试玩维护开始时间
   */
  @TableField(updateStrategy = FieldStrategy.IGNORED)
  private Date demoMaintenanceTimeStart;


  /**
   * 试玩维护结束时间
   */
  @TableField(updateStrategy = FieldStrategy.IGNORED)
  private Date demoMaintenanceTimeEnd;


  /**
   * 创建时间
   */
  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 创建者
   */
  @TableField(fill = FieldFill.INSERT)
  @ApiModelProperty(value = "创建者")
  private String createBy;

  /**
   * 更新时间
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  /**
   * 更新者
   */
  @TableField(fill = FieldFill.INSERT_UPDATE)
  @ApiModelProperty(value = "更新者")
  private String updateBy;

}
