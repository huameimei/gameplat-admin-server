package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * banner图片信息
 *
 * @author admin
 */
@Data
@TableName("sys_banner_info")
public class SysBannerInfo {

  /** 主键 */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /** banner类型 */
  private Integer bannerType;

  /** banner子类型 */
  private Long childType;

  /** banner子类型名称 */
  private String childName;

  /** pc端图片地址 */
  private String pcPicUrl;

  /** app端图片地址 */
  private String appPicUrl;

  /** 状态 */
  private Integer status;

  /** 创建时间 */
  private Date createTime;

  /** 创建人 */
  private String createBy;

  /** 更新时间 */
  private Date updateTime;

  /** 更新人 */
  private String updateBy;

  /** 备注 */
  private String remark;

  /** 语种 */
  private String language;

  /** 跳转地址 */
  private String jumpUrl;

  /** 游戏类别 */
  private String gameKind;

  /** 关联游戏 */
  private String gameCode;
}
