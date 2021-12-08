package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author lily
 * @description 会员俸禄派发详情入参
 * @date 2021/11/27
 */

@Data
public class MemberWealDetailRemoveDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "福利表主键")
    private Long wealId;

    @ApiModelProperty(value = "会员id")
    private Long userId;
}
