package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 值类型VO
 *
 * @author admin
 */
@Data
@ApiModel("值类型VO")
public class ValueDataVO {

    @ApiModelProperty(value = "数值")
    private String value;

    @ApiModelProperty(value = "名称")
    private String name;

}
