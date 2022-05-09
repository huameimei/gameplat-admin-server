package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author key @Date 2022/3/10
 */
@Data
public class SysFloatTypeVo implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 主键 */
  private Integer id;

  /** 租户 */
  private Integer tenant;

  /** 终端类型(web/mobile) */
  private String osType;

  /** 浮窗类型（square/round） */
  private String floatType;

  /** 浮窗排序 */
  private Integer floatIndex;

  /** 是否开启(0否/1是) */
  private Integer whetherOpen;

  /** 是否可以拖拽(0否/1是) */
  private Integer canDrag;

  /** 是否可以关闭(0否/1是) */
  private Integer canClose;

  /** 跳转地址 */
  private String jumpUrl;
  /** 图片地址 */
  private String iconPath;

  /** 浮窗名字 */
  private String floatName;

  /** 展示位置 */
  private String showPosition;

  /** 创建时间 */
  private Date createTime;

  /** 更新时间 */
  private Date updateTime;

  private List<SysFloatSettingVo> settingList;

  private List<String> showPositionList;
}
