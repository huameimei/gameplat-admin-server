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
public class MemberWealDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "福利表主键")
    private Long wealId;

    @ApiModelProperty(value = "会员账号")
    private String userName;

    @ApiModelProperty(value = "1：未派发   2：已派发  3:已回收")
    private Integer status;





}
