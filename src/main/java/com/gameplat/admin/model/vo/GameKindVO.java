package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
public class GameKindVO implements Serializable {

  private Long id;
  /** 游戏分类编码 */
  private String code;

  /** 游戏分类名称 */
  private String name;

  /** 游戏分类显示名称 */
  private String showName;

  /** 平台编码 */
  private String platformCode;

  /** 是否开放试玩(0：不支持；1:关闭 2:开启) */
  private Integer demoEnable;

  /** 是否开放(0：否；1:是) */
  private Integer enable;

  /** 是否热门(0：否；1:是) */
  private Integer hot;

  /** 是否有下级游戏(0：否；1:是) */
  private Integer subLevel;

  /** 排序 */
  private Integer sort;

  /** PC图片地址 */
  private String pcPicUrl;

  /** 手机图片地址 */
  private String appPicUrl;

  /** 游戏大类 */
  private String gameType;

  /** 返水最高比例 */
  private BigDecimal rebateRate;

  /** 维护开始时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date maintenanceTimeStart;

  /** 维护结束时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date maintenanceTimeEnd;

  /** 试玩维护开始时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date demoMaintenanceTimeStart;

  /** 试玩维护结束时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date demoMaintenanceTimeEnd;

  /** 更新时间 */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  /** 是否外跳(0：否；1:是) */
  private Integer isJump;

  /** 是否竖屏(0：否；1:是) */
  private Integer isVertical;

  /** 运营状态(0已下架、1下架中、2运营中) */
  private Integer status;
}
