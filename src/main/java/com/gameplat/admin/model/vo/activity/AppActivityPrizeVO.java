package com.gameplat.admin.model.vo.activity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author bhf
 * @Description 活动信息奖项
 * @Date 2020/6/11 16:51
 **/
@Data
public class AppActivityPrizeVO {

    @JsonSerialize(using= ToStringSerializer.class)
	@ApiModelProperty(value = "奖品id")
	private Long activityPrizeId;

    @ApiModelProperty(value = "中奖概率")
    private Integer prizeChance;

    @ApiModelProperty(value = "中奖数量")
    private Integer giveAmount;

    @ApiModelProperty(value = "奖品库存")
    private Integer prizeRepertory;

    @ApiModelProperty(value = "一批次发放总数量")
    private Integer onceTotalNum;

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

}
