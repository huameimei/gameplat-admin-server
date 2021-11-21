package com.gameplat.admin.model.domain.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 沙漠、
 * @since 2020-07-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Financial对象", description="")
public class Financial2 implements Serializable {

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

    @ApiModelProperty(value = "来源id 1 充值、2 提现、3 转账、4 红利 、5 返水 、" +
            "6 提现退回 、 9 代充 、11 加币 、 " +
            "8 减币 、 18 减币退回 、19 直播 、 20 好友推荐 、14 游戏交易 、21 返佣）")
    private Integer sourceType;

    @ApiModelProperty(value = "来源类型")
    private String sourceId;

    @ApiModelProperty(value = "订单状态 1、 2-受理中；3-已入款；4-已取消 ")
    private Integer state;

    @ApiModelProperty(value = "交易方式 1 - 转账汇款, 2 - 在线支付, 3 - 后台入款")
    private Integer mode;

    @ApiModelProperty(value = "银行卡号")
    private String bankNo;

    @ApiModelProperty(value = "租户名称")
    private String tenantName;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "货币名称")
    private String currencyName;

    @ApiModelProperty(value = "货币种类")
    private Integer currencyType;

    @ApiModelProperty(value = "动前余额")
    private BigDecimal beforeBalance;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "动后余额")
    private BigDecimal afterBalance;

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

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}
