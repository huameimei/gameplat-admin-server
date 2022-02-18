package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
* @author lily
* @date 2021/12/29
*/
@Data
public class IpAnalysisVO {

	@ApiModelProperty(value = "会员账号")
	private String account;

//	@ApiModelProperty(value = "会员姓名")
//	private String fullName;
//
//	@ApiModelProperty(value = "会员id")
//	private Long memberId;

	@ApiModelProperty(value = "ip地址")
	private String ipAddress;

	@ApiModelProperty(value = "ip次数")
	private Integer ipCount;

	@ApiModelProperty(value = "ip城市")
	private String loginAddress;

	@ApiModelProperty(value = "在线状态 0不在线 1在线")
	private Integer offline;

	@ApiModelProperty(value = "在线状态 0不在线 1在线")
	private String uuid;

//	@ApiModelProperty(value = "账户余额")
//	private BigDecimal money;
}
