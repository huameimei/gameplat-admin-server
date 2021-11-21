package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 *@author lyq
 *@Description
 *@date 2020年6月17日 下午3:55:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberTurntableWinRecordVO implements Serializable{/**
	 *
	 */
	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "奖品名称")
    private String prizeName;

    @ApiModelProperty(value = "中获数量")
    private Integer num;

    @ApiModelProperty(value = "创建时间")
    private String createTime;
}
