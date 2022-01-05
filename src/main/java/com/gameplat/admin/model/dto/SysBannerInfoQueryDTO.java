package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * banner信息DTO
 *
 * @author admin
 */
@Data
public class SysBannerInfoQueryDTO implements Serializable {

    /**
     * 语种
     */
    @ApiModelProperty("语种")
    private String language;

}
