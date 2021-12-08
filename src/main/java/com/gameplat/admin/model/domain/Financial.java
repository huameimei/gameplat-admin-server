package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author lily
 * @since 2021-11-27
 */
@Data
@TableName("financial")
public class Financial implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "financial_id", type = IdType.AUTO)
    private Long financialId;

    @ApiModelProperty(value = "流水单号")
    private String flowNo;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "来源类型 1 充值、2 提现、3 转账、4 红利 、5 返水 、" +
            "6 提现退回 、 9 代充 、11 加币 、 " +
            "8 减币 、 18 减币退回 、19 直播 、 20 好友推荐 、14 游戏交易、15 游戏交易-转出、16 游戏交易-转入）")
    private Integer sourceType;

    @ApiModelProperty(value = "来源类型")
    private String sourceId;

    @ApiModelProperty(value = "订单状态 1、 2-受理中；3-已入款；4-已取消 ")
    private Integer state;

    @ApiModelProperty(value = "交易方式 1 - 转账汇款, 2 - 在线支付, 3 - 后台入款")
    private Integer mode;

    @ApiModelProperty(value = "银行卡号")
    private String bankNo;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "货币名称")
    private String currencyName;

    @ApiModelProperty(value = "货币种类")
    private Integer currencyType;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "优惠金额")
    private BigDecimal disAmount;

    @ApiModelProperty(value = "余额（人民币）")
    private BigDecimal amountCny;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    @ApiModelProperty(value = "是否扣除资产（0 否 1是）")
    private Integer isLess;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "手续费")
    private BigDecimal counterFee;

    @ApiModelProperty(value = "租户名称")
    private String tenantName;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新人")
    private Date updateTime;

    @ApiModelProperty(value = "每天日期")
    private Date today;

    @ApiModelProperty(value = "收入")
    private BigDecimal income;

    @ApiModelProperty(value = "支出")
    private BigDecimal expenditure;

    @ApiModelProperty(value = "充值汇率")
    private BigDecimal rate;

    @ApiModelProperty(value = "充值货币数量")
    private BigDecimal rechargeNum;


    @ApiModelProperty(value = "充值类型")
    private String payType;

    @ApiModelProperty(value = "手續費")
    private String fee;

    @ApiModelProperty(value = "到账金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "提现被取消原因")
    private String cancelReason;

    @ApiModelProperty(value = "会员备注")
    private String userRemark;
}
