package com.gameplat.admin.model.dto.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 *@author lyq
 *@Description 奖品DTO
 *@date 2020年5月29日 下午1:52:40
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberPrizeDTO extends com.live.cloud.backend.model.activity.dto.QueryCommomDTO implements Serializable{/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
    private Long[] ids;

	@ApiModelProperty(value = "主键")
    private Long prizeId;

	@ApiModelProperty(value = "转盘id")
	private Long activityId;

    @ApiModelProperty(value = "奖品类型")
    private String prizeType;

    @ApiModelProperty(value = "奖品等级")
    private String prizeLevel;

    @ApiModelProperty(value = "奖品图片")
    private String prizeIcon;

    @ApiModelProperty(value = "奖品名称")
    private String prizeName;

    @ApiModelProperty(value = "券码")
    private String ticketYard;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态 0下线 1上线")
    private Integer status;

    @ApiModelProperty(value = "活动类型（1红包雨，2转盘）")
    private Integer type;


}
