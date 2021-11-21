package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
*
* @author 沙漠
* @date 2020年7月9日 上午10:37:42
*/
@Data
public class AppTurntableUserLuckDrawVO {

	@ApiModelProperty(value = "中奖信息")
	private List<AppActivityPrizeVO> winningList;

	@ApiModelProperty(value = "幸运值")
	private Integer luckValue;

}
