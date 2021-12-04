package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/** 会员信息 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("member_info")
public class MemberInfo {

  @TableId
  @ApiModelProperty(value = "会员ID")
  private Long memberId;

  @ApiModelProperty(value = "会员模式 1现金、2信用")
  private Integer userMode;

  @ApiModelProperty(value = "是否发放俸禄 0--否，1--是")
  private Integer salaryFlag;

  @ApiModelProperty(value = "是否限制彩票投注 0--否， 1--是")
  private Integer limitLotFlag;

  @ApiModelProperty(value = "推广码")
  private String invitationCode;

  @ApiModelProperty(value = "密保ID")
  private Long safetyId;

  @ApiModelProperty(value = "省市区")
  private String province;

  @ApiModelProperty(value = "街道")
  private String street;

  @ApiModelProperty(value = "收货人")
  private String consignee;

  @ApiModelProperty(value = "收货人电话")
  private String contactPhone;

  @ApiModelProperty(value = "提款密码")
  private String cashPassword;

  @ApiModelProperty(value = "提现盐")
  private String cashSalt;

  @ApiModelProperty(value = "会员积分")
  private Integer totalPoint;

  @ApiModelProperty(value = "返点百分比")
  private String rebate;

  @ApiModelProperty(value = "余额")
  private BigDecimal balance;

  @ApiModelProperty(value = "冻结金额")
  private BigDecimal freeze;

  @ApiModelProperty(value = "余额宝金额")
  private BigDecimal yubaoAmount;

  @ApiModelProperty(value = "余额宝金额最近变动时间")
  private Date yubaoTime;

  @ApiModelProperty(value = "首次充值时间")
  private Date firstRechTime;

  @ApiModelProperty(value = "首次充值金额")
  private BigDecimal firstRechAmount;

  @ApiModelProperty(value = "首次取款时间")
  private Date firstWithdrawTime;

  @ApiModelProperty(value = "首次取款金额")
  private BigDecimal firstWithdrawAmount;

  @ApiModelProperty(value = "最近登录IP")
  private String lastLoginIp;

  @ApiModelProperty(value = "最近登陆时间")
  private Date lastLoginTime;

  private Date lastRechTime;

  private BigDecimal lastRechAmount;

  private Date lastWithdrawTime;

  private BigDecimal lastWithdrawAmount;

  private BigDecimal totalRechAmount;

  private Integer totalRechTimes;

  private BigDecimal totalWithdrawAmount;

  private Integer totalWithdrawTimes;

  @Version private Integer version;
}
