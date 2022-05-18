package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内信
 *
 * @author: kenvin
 * @date: 2021/4/28 15:53
 * @desc:
 */
@Data
public class MessageInfoVO implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 主键 */
  @Schema(description = "主键ID")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  @Schema(description = "消息标题")
  private String title;

  @Schema(description = "消息内容")
  private String content;

  @Schema(description = "总类别:0.默认(3) 1.游戏(2) 2.足球(2) 3.直播(2) 4.系统(1)")
  private Integer category;

  @Schema(description = "位置: 0.默认(1) 1.推荐(2) 2.首页大厅(2,3) 3.彩票首页(3) 4.体育首页(3) 5.游戏首页(3)")
  private Integer position;

  @Schema(description = "展示类型: 0.默认(1) 1.滚动(2) 2.文本弹窗(3) 3.图片弹窗(3)")
  private Integer showType;

  @Schema(description = "PC端图片")
  private String pcImage;

  @Schema(description = "APP端图片")
  private String appImage;

  @Schema(description = "弹出次数:0.默认(1,2) 1.只弹一次(3) 2.多次弹出(3)")
  private Integer popsCount;

  @Schema(description = "推送范围:1.全部会员 2.部分会员 3.在线会员 4.充值层级 5.VIP等级 6.代理线")
  private Integer pushRange;

  @Schema(description = "关联账号")
  private String linkAccount;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  @Schema(description = "开始时间")
  private Date beginTime;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  @Schema(description = "结束时间")
  private Date endTime;

  @Schema(description = "升序排序")
  private Integer sort;

  @Schema(description = "消息类型（1.系统消息、2.平台公告、3.个人弹窗消息）")
  private Integer type;

  @Schema(description = "语种")
  private String language;

  @Schema(description = "状态：0 禁用 1 启用")
  private Integer status;

  @Schema(description = "是否即时消息: 0 否  1 是")
  private Integer immediateFlag;

  @Schema(description = "备注")
  private String remarks;

  @Schema(description = "创建人")
  private String createBy;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "更新人")
  private String updateBy;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  @Schema(description = "更新时间")
  private Date updateTime;
}
