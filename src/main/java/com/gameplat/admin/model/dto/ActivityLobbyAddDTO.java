package com.gameplat.admin.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gameplat.base.common.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 活动大厅新增DTO <br>
 *
 * @Author: kenvin <br>
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityLobbyAddDTO", description = "活动大厅新增DTO")
public class ActivityLobbyAddDTO implements Serializable {

    private static final long serialVersionUID = 6060013282905693277L;

    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(value = "标题")
    private String title;

    @NotNull(message = "开始时间不能为空")
    @ApiModelProperty(value = "开始时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private Date startTime;

    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = DateUtil.YYYY_MM_DD_HH_MM_SS)
    private Date endTime;

    @ApiModelProperty(value = "活动板块")
    private Long type;

    @NotNull(message = "活动类型必选")
    @Range(min = 1, max = 3, message = "活动类型的值必须在1-3之间")
    @ApiModelProperty(
            value =
                    "活动类型（1 充值活动(统计项目(statisItem)选项中的1,2,3,4,5项可选)，"
                            + "2 游戏活动(统计项目(statisItem)选项中的6,7,8,9,10项可选)")
    private Integer activityType;

    @NotBlank(message = "描述不能为空")
    @ApiModelProperty(value = "描述")
    private String description;

    @Range(min = 1, max = 12, message = "统计项目值须在1-12之间")
    @NotNull(message = "统计项目必选")
    @ApiModelProperty(
            value =
                    "统计项目（1 累计充值金额，2 累计充值天数，3 连续充值天数，4 单日首充金额，5 首充金额，6 累计彩票打码金额，7 累计彩票打码天数，"
                            + "8 连续彩票打码天数，9 单日彩票亏损金额，10 累计体育打码金额，11累计体育打码天数，11 连续体育打码天数，12 单日体育亏损金额）")
    private Integer statisItem;

    @ApiModelProperty(value = "充值类型（1 转账汇款，2 在线支付，3 人工入款）")
    private String payType;

    @NotNull(message = "统计日期不能为空")
    @Range(min = 1, max = 5, message = "统计日期值须在1-5之间")
    @ApiModelProperty(value = "统计日期（1 每日，2 每周，3 每月，4 每周X，5 每月X日）")
    private Integer statisDate;

    @ApiModelProperty(value = "详细日期（日），周日为每周开始的第一天，下标为1")
    private Integer detailDate;

    @NotBlank(message = "参与层级不能为空")
    @ApiModelProperty(value = "参与层级")
    private String graded;

    @NotNull(message = "申请方式不能为空")
    @Range(min = 1, max = 2, message = "申请方式值必须在1-2之间")
    @ApiModelProperty(value = "申请方式（1 手动，2 自动）")
    private Integer applyWay;

    @ApiModelProperty(value = "审核方式（1 手动，2 自动）")
    private Integer auditWay;

    @ApiModelProperty(value = "隔天申请（0 否，1 是）（在有效统计时间的次日申请活动彩金)")
    private Integer nextDayApply;

    @ApiModelProperty(value = "多重彩金（0 否，1 是）（可领取所有满足等级要求的活动彩金）")
    private Integer multipleHandsel;

    @ApiModelProperty(value = "账号黑名单")
    private String accountBlacklist;

    @ApiModelProperty(value = "ip黑名单")
    private String ipBlacklist;

    @Size(min = 1)
    @NotEmpty(message = "活动大厅优惠列表不能为空")
    @ApiModelProperty(value = "活动大厅优惠")
    private List<ActivityLobbyDiscountDTO> lobbyDiscountList;

    @ApiModelProperty(value = "失效活動")
    private Integer failure;

    @ApiModelProperty(value = "领取方式（1 红包，2 通知）")
    private Integer getWay;

    @NotBlank(message = "用户VIP等级不能为空")
    @ApiModelProperty(value = "用户VIP等级")
    private String userLevel;

    @NotNull(message = "活动状态必选")
    @ApiModelProperty(value = "活动状态（0 关闭，1 开启，2 失效）")
    private Integer status;

    @ApiModelProperty(value = "游戏类型")
    private String gameType;

    @ApiModelProperty(value = "游戏列表")
    private String gameList;

    @ApiModelProperty(value = "有效充值金额")
    private BigDecimal rechargeValidAmount;

    @ApiModelProperty(value = "有效体育打码金额")
    private BigDecimal sportValidAmount;

    @ApiModelProperty(value = "指定比赛ID")
    private Long matchId;

    @ApiModelProperty(value = "指定比赛时间")
    private Date matchTime;

    @ApiModelProperty(value = "指定比赛类型")
    private Integer matchType;

    @ApiModelProperty(value = "是否可以重复参加")
    private Integer isRepeat;

    @ApiModelProperty(value = "是否自动派发")
    private Integer isAutoDistribute;

    @ApiModelProperty(value = "申请日期集合")
    private String applyDateList;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "目标值")
    private String targetValue;
}
