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

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "真实姓名")
	private String idName;

	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "ip地址")
	private String loginIp;

	@ApiModelProperty(value = "ip次数")
	private Integer loginTime;

	@ApiModelProperty(value = "ip城市")
	private String loginAddress;

	@ApiModelProperty(value = "在线状态 0不在线 1在线")
	private Integer offline;

	@ApiModelProperty(value = "余额")
	private BigDecimal balance;

}
