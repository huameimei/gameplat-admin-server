package com.gameplat.admin.model.entity;


import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;


/**
 * 会员扩展信息表
 */
@Data
@Builder
public class MemeberInfoExpand {

  @ApiModelProperty(value = "主键")
  private Long expId;

  @ApiModelProperty(value = "会员ID")
  private Long userId;

  @ApiModelProperty(value = "会员层级或代理层级")
  private Integer userLevel;

  @ApiModelProperty(value = "充值层级")
  private Integer rechLevel;

  @ApiModelProperty(value = "会员模式 1现金、2信用")
  private Integer userMode;

  @ApiModelProperty(value = "是否发放俸禄 0--否，1--是")
  private Integer provideSalaryFlag;

  @ApiModelProperty(value = "是否限制彩票投注 0--否， 1--是")
  private Integer limitLotFlag;

  @ApiModelProperty(value = "彩票盘口Id")
  private Long pankouId;

  @ApiModelProperty(value = "是否限制提款")
  private Integer  limitWithDrawFlag;

  @ApiModelProperty(value = "推广码")
  private String intrCode;

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
  private String totalPoint;

  @ApiModelProperty(value = "返点百分比")
  private Double rebate;

  @ApiModelProperty(value = "余额")
  private Double balance;

  @ApiModelProperty(value = "冻结金额")
  private Double freeze;

  @ApiModelProperty(value = "余额宝金额")
  private Double yubaoMoney;

  @ApiModelProperty(value = "余额宝金额最近变动时间")
  private LocalDateTime yubaoTime;

  @ApiModelProperty(value = "首次充值时间")
  private LocalDateTime firstRechTime;

  @ApiModelProperty(value = "首次充值金额")
  private Double firstRechMoney;

  @ApiModelProperty(value = "首次取款时间")
  private LocalDateTime firstWithdrawTime;

  @ApiModelProperty(value = "首次取款金额")
  private Double firstWithdrawMoney;

  @ApiModelProperty(value = "最近登录IP")
  private String lastLoginIp;

  @ApiModelProperty(value = "最近登陆时间")
  private LocalDateTime lastLoginTime;







}
