package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 站内信新增DTO
 *
 * @author: kenvin
 * @date: 2021/4/28 15:53
 * @desc:
 */
@Data
public class MessageInfoAddDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "消息标题不能为空")
  @Schema(description = "消息标题")
  private String title;

  @NotNull(message = "消息内容不能为空")
  @Schema(description = "消息内容")
  private String content;

  @Schema(description = "总类别:0.默认(3) 1.游戏(2) 2.足球(2) 3.直播(2) 4.系统(1)")
  private Integer category;

  @Schema(description = "位置: 0.默认(1) 1.推荐(2) 2.首页大厅(2,3) 3.彩票首页(3) 4.体育首页(3) 5.游戏首页(3)")
  private Integer position;

  @NotNull(message = "展示类型不能为空")
  @Schema(description = "展示类型: 0.默认(1) 1.滚动(2) 2.文本弹窗(3) 3.图片弹窗(3)")
  private Integer showType;

  @Schema(description = "PC端图片")
  private String pcImage;

  @Schema(description = "APP端图片")
  private String appImage;

  @NotNull(message = "弹出次数不能为空")
  @Schema(description = "弹出次数:0.默认(1,2) 1.只弹一次(3) 2.多次弹出(3)")
  private Integer popsCount;

  @NotNull(message = "推送范围不能为空")
  @Min(value = 1, message = "推送范围必须大于0")
  @Schema(description = "推送范围:1.全部会员 2.部分会员 3.在线会员 4.充值层级 5.VIP等级 6.代理线")
  private Integer pushRange;

  @Schema(description = "关联账号")
  private String linkAccount;

  @NotNull(message = "开始时间不能为空")
  @Schema(description = "开始时间")
  private String beginTime;

  @NotNull(message = "结束时间不能为空")
  @Schema(description = "结束时间")
  private String endTime;

  @Schema(description = "升序排序")
  private Integer sort;

  @NotNull(message = "消息类型不能为空")
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

  @Schema(description = "意见反馈类型 0:活动建议 1:功能建议 2:游戏BUG 3:其他问题")
  private String feedbackType;

  /** 更新人 */
  @Schema(description = "意见反馈回复图片内容")
  private String feedbackImage;
}
