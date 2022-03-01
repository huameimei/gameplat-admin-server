package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import java.io.Serializable;

/**
 * @author lily
 * @description 金币
 * @date 2022/3/1
 */
@Data
@AssertTrue
public class MemberGoldCoinRecordQueryDTO implements Serializable {

    @ApiModelProperty(value = "订单号")
    private Long memberId;

    @ApiModelProperty(value = "玩家名字")
    private String account;

    @ApiModelProperty(value = "来源类型（待定）1 获取成长值、2 爆料扣款")
    private Integer sourceType;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
