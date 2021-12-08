package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 活动大厅
 *
 * @author lyq
 * @Description 实体层
 * @date 2020-08-14 14:50:01
 */
@Data
@TableName("activity_lobby")
public class ActivityLobby implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 活动大类（1 活动大厅，2 红包雨，3 转盘）
     */
    private Integer type;

    /**
     * 描述
     */
    private String description;

    /**
     * 统计项目（1 累计充值金额，2 累计充值天数，3 连续充值天数，4 单日首充金额，5 首充金额）
     */
    private Integer statisItem;

    /**
     * 充值类型（1 转账汇款，2 在线支付，3 人工入款）
     */
    private String payType;

    /**
     * 统计日期（1 每日，2 每周，3 每月，4 每周X，5 每月X日）
     */
    private Integer statisDate;

    /**
     * 详细日期（日）
     */
    private Integer detailDate;

    /**
     * 参与层级
     */
    private String graded;

    /**
     * 申请方式（1 手动，2 自动）
     */
    private Integer applyWay;

    /**
     * 审核方式（1 手动，2 自动）
     */
    private Integer auditWay;

    /**
     * 隔天申请（0 否，1 是）（在有效统计时间的次日申请活动彩金)
     */
    private Integer nextDayApply;

    /**
     * 多重彩金（0 否，1 是）（可领取所有满足等级要求的活动彩金）
     */
    private Integer multipleHandsel;

    /**
     * 账号黑名单
     */
    private String accountBlacklist;

    /**
     * ip黑名单
     */
    private String ipBlacklist;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 申请路径
     */
    private String applyUrl;

    /**
     * 领取方式（1 直接发放，2 福利中心）
     */
    private Integer getWay;

    /**
     * 用户等级
     */
    private String userLevel;

    /**
     * 活动状态（0 关闭，1 开启，2 失效）
     */
    private Integer status;

    /**
     * 游戏列表
     */
    private String gameList;

    /**
     * 有效充值金额
     */
    private BigDecimal rechargeValidAmount;

    /**
     * 有效体育打码金额
     */
    private BigDecimal sportValidAmount;

    /**
     * 指定比赛ID
     */
    private Long matchId;

    /**
     * 指定比赛类型
     */
    private Integer matchType;

    /**
     * 是否可以重复参加
     */
    private Integer isRepeat;

    /**
     * 是否自动派发
     */
    private Integer isAutoDistribute;

    /**
     * 申请日期集合
     */
    private String applyDateList;
}
