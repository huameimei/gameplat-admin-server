package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author lily
 * @version 2021-11-25
 * 会员日报表
 */
@Data
public class MemberRWReportVo implements Serializable {

	private static final long serialVersionUID = 1L;



	@ApiModelProperty(value = "会员账号")
	private String userName;

	/*@ApiModelProperty(value = "代理账号")
	private String parentName;
*/

	@ApiModelProperty(value = "转账次数")
	private Integer bankCount;

	@ApiModelProperty(value = "转账金额")
	private BigDecimal bankMoney;

	@ApiModelProperty(value = "在线支付次数")
	private Integer onlineCount;

	@ApiModelProperty(value = "在线支付总金额")
	private BigDecimal onlineMoney;

	@ApiModelProperty(value = "人工充值次数")
	private Integer handRechCount;

	@ApiModelProperty(value = "人工充值总金额")
	private BigDecimal handRechMoney;

	@ApiModelProperty(value = "首次充值金额")
	private BigDecimal firstRechMoney;

	@ApiModelProperty(value = "优惠金额")
	private BigDecimal rechDiscount;

	@ApiModelProperty(value = "其他优惠金额")
	private BigDecimal otherDiscount;

	@ApiModelProperty(value = "会员出款次数")
	private Integer withdrawCount;

	@ApiModelProperty(value = "会员出款金额")
	private BigDecimal withdrawMoney;

	@ApiModelProperty(value = "人工出款次数")
	private Integer handWithdrawCount;

	@ApiModelProperty(value = "人工出款金额")
	private BigDecimal handWithdrawMoney;

	@ApiModelProperty(value = "首次出款金额")
	private BigDecimal firstWithdrawMoney;


	@ApiModelProperty(value = "手续费")
	private BigDecimal counterFee;

	@ApiModelProperty(value = "充值金额总额")
	private BigDecimal totailRechargeAmount;

	@ApiModelProperty(value = "优惠金额总额")
	private BigDecimal totailDiscoutAmount;

	@ApiModelProperty(value = "提现金额")
	private BigDecimal totailWithdrawAmount;

	@ApiModelProperty(value = "充提总结余")
	private BigDecimal totalRWAmount;


}
