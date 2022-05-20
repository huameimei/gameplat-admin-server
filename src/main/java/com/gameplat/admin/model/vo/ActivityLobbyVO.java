package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/** 活动大厅VO @Author: lyq @Date: 2020/8/14 15:22 @Description: */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityLobbyVO implements Serializable {

  private static final long serialVersionUID = 3763472307641460049L;

  @JsonSerialize(using = ToStringSerializer.class)
  @Schema(description = "主键")
  private Long id;

  @Schema(description = "标题")
  private String title;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  @Schema(description = "开始时间")
  private Date startTime;

  @JsonSerialize(using = Date2LongSerializerUtils.class)
  @Schema(description = "结束时间")
  private Date endTime;

  @Schema(description = "活动大类")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long type;

  @Schema(
      description =
          "活动类型（1 充值活动(统计项目(statisItem)选项中的1,2,3,4,5项可选)；"
              + "2 游戏活动(统计项目(statisItem)选项中的6,7,8,9,10,11,12项可选)")
  private Integer activityType;

  @Schema(description = "描述")
  private String description;

  @Schema(
      description =
          "统计项目（1 累计充值金额，2 累计充值天数，3 连续充值天数，4 单日首充金额，5 首充金额，6 累计彩票打码金额，7 累计彩票打码天数，"
              + "8 连续彩票打码天数，9 单日彩票亏损金额，10 累计体育打码金额，11累计体育打码天数，11 连续体育打码天数，12 单日体育亏损金额）")
  private Integer statisItem;

  @Schema(description = "充值类型（1 转账汇款，2 在线支付，3 人工入款）")
  private String payType;

  @Schema(description = "统计日期（1 每日，2 每周，3 每月，4 每周X，5 每月X日）")
  private Integer statisDate;

  @Schema(description = "详细日期（日）")
  private Integer detailDate;

  @Schema(description = "参与层级")
  private String graded;

  @Schema(description = "申请方式（1 手动，2 自动）")
  private Integer applyWay;

  @Schema(description = "审核方式（1 手动，2 自动）")
  private Integer auditWay;

  @Schema(description = "隔天申请（0 否，1 是）（在有效统计时间的次日申请活动彩金)")
  private Integer nextDayApply;

  @Schema(description = "多重彩金（0 否，1 是）（可领取所有满足等级要求的活动彩金）")
  private Integer multipleHandsel;

  @Schema(description = "账号黑名单")
  private String accountBlacklist;

  @Schema(description = "ip黑名单")
  private String ipBlacklist;

  @Schema(description = "备注")
  private String remark;

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

  @Schema(description = "领取方式（1 红包，2 通知）")
  private Integer getWay;

  @Schema(description = "活动大厅优惠")
  private List<ActivityLobbyDiscountVO> lobbyDiscountList;

  @Schema(description = "用户VIP等级")
  private String userLevel;

  @Schema(description = "活动状态（0 关闭，1 开启，2 失效）")
  private Integer status;

  @Schema(description = "游戏类型（1体育 2真人 3棋牌 4彩票 5电子 6电竞 7动物竞技 8捕鱼）")
  private String gameType;

  @Schema(description = "游戏列表")
  private String gameList;

  @Schema(description = "有效充值金额")
  private BigDecimal rechargeValidAmount;

  @Schema(description = "有效体育打码金额")
  private BigDecimal sportValidAmount;

  @Schema(description = "指定比赛ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private Long matchId;

  @Schema(description = "指定比赛类型")
  private Integer matchType;

  @Schema(description = "是否可以重复参加")
  private Integer isRepeat;

  @Schema(description = "是否自动派发")
  private Integer isAutoDistribute;

  @Schema(description = "申请日期集合")
  private String applyDateList;
}
