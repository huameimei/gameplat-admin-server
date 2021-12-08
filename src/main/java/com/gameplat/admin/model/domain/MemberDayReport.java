package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author lily
 * @version 2021-11-25
 * 会员日报表
 */
@Data
@TableName("member_day_report")
public class MemberDayReport implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "统计日期")
	private Date countDate;

	@ApiModelProperty(value = "会员ID")
	private Long userId;

	@ApiModelProperty(value = "会员账号")
	private String userName;

	@ApiModelProperty(value = "会员父级ID")
	private Long parentId;

	@ApiModelProperty(value = "会员账号")
	private String parentName;

	@ApiModelProperty(value = "用户类型")
	private Integer userType;

	@ApiModelProperty(value = "会员账号")
	private String agentPath;

	@ApiModelProperty(value = "充值次数")
	private int rechargeCount;

	@ApiModelProperty(value = "充值金额")
	private BigDecimal rechargeAmount;

	@ApiModelProperty(value = "优惠金额")
	private BigDecimal discountAmount;

	@ApiModelProperty(value = "彩金")
	private BigDecimal jackpotAmount;

	@ApiModelProperty(value = "提现次数")
	private int withdrawCount;

	@ApiModelProperty(value = "提现金额")
	private BigDecimal withdrawAmount;

	@ApiModelProperty(value = "手续费")
	private BigDecimal feeAmount;

	@ApiModelProperty(value = "投注额")
	private BigDecimal betAmount;

	@ApiModelProperty(value = "有效投注额")
	private BigDecimal validAmount;

	@ApiModelProperty(value = "输赢金额")
	private BigDecimal winAmount;

	@ApiModelProperty(value = "返水金额")
	private BigDecimal waterAmount;

	@ApiModelProperty(value = "首冲金额")
	private BigDecimal firstRechargeAmount;

	@ApiModelProperty(value = "首提金额")
	private BigDecimal firstWithdrawAmount;

	@ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	@TableField(fill = FieldFill.UPDATE)
	private Date updateTime;

	@ApiModelProperty(value = "非正常入款")
	private BigDecimal abnormalIncome;

	@ApiModelProperty(value = "非正常出款")
	private BigDecimal abnormalOutcome;

	@ApiModelProperty(value = "彩票注单数")
	private Integer lotteryOrderCount;

	@ApiModelProperty(value = "体育注单数")
	private Integer sportOrderCount;

	@ApiModelProperty(value = "真人注单数")
	private Integer realOrderCount;

	@ApiModelProperty(value = "彩票中奖订单数")
	private Integer lotteryWinOrderCount;

	@ApiModelProperty(value = "体育中奖订单数")
	private Integer sportWinOrderCount;

	@ApiModelProperty(value = "真人中奖订单数")
	private Integer realWinOrderCount;

	@ApiModelProperty(value = "彩票有效投注额")
	private BigDecimal lotteryValidAmount;

	@ApiModelProperty(value = "体育有效投注额")
	private BigDecimal sportValidAmount;

	@ApiModelProperty(value = "真人有效投注额")
	private BigDecimal realValidAmount;

	@ApiModelProperty(value = "彩票输赢金额")
	private BigDecimal lotteryWinAmount;

	@ApiModelProperty(value = "体育输赢金额")
	private BigDecimal sportWinAmount;

	@ApiModelProperty(value = "真人输赢金额")
	private BigDecimal realWinAmount;

	@ApiModelProperty(value = "彩票返水金额")
	private BigDecimal lotteryWaterAmount;

	@ApiModelProperty(value = "体育返水金额")
	private BigDecimal sportWaterAmount;

	@ApiModelProperty(value = "真人返水金额")
	private BigDecimal realWaterAmount;

	@ApiModelProperty(value = "彩票盈利率")
	private Double lotteryProfitRate;

	@ApiModelProperty(value = "体育盈利率")
	private Double sportProfitRate;

	@ApiModelProperty(value = "真人盈利率")
	private Double realProfitRate;

	@ApiModelProperty(value = "彩票中奖率")
	private Double lotteryWinRate;

	@ApiModelProperty(value = "体育中奖率")
	private Double sportWinRate;

	@ApiModelProperty(value = "真人中奖率")
	private Double realWinRate;

	public MemberDayReport() {
		this.rechargeCount = 0;
		this.rechargeAmount = new BigDecimal(0);
		this.discountAmount = new BigDecimal(0);
		this.jackpotAmount = new BigDecimal(0);
		this.withdrawCount = 0;
		this.withdrawAmount = new BigDecimal(0);
		this.feeAmount = new BigDecimal(0);
		this.betAmount = new BigDecimal(0);
		this.validAmount = new BigDecimal(0);
		this.winAmount = new BigDecimal(0);
		this.waterAmount = new BigDecimal(0);
		this.firstRechargeAmount = new BigDecimal(0);
		this.firstWithdrawAmount = new BigDecimal(0);
		this.abnormalIncome = new BigDecimal(0);
		this.abnormalOutcome = new BigDecimal(0);
		this.lotteryOrderCount = 0;
		this.sportOrderCount = 0;
		this.realOrderCount = 0;
		this.lotteryWinOrderCount = 0;
		this.sportWinOrderCount = 0;
		this.realWinOrderCount = 0;
		this.lotteryValidAmount = new BigDecimal(0);
		this.sportValidAmount = new BigDecimal(0);
		this.realValidAmount = new BigDecimal(0);
		this.lotteryWinAmount = new BigDecimal(0);
		this.sportWinAmount = new BigDecimal(0);
		this.realWinAmount = new BigDecimal(0);
		this.lotteryWaterAmount = new BigDecimal(0);
		this.sportWaterAmount = new BigDecimal(0);
		this.realWaterAmount = new BigDecimal(0);
		this.lotteryProfitRate = 0.00;
		this.sportProfitRate = 0.00;
		this.realProfitRate = 0.00;
		this.lotteryWinRate = 0.00;
		this.sportWinRate = 0.00;
		this.realWinRate = 0.00;
	}
}
