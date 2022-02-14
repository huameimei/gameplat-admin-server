package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/13
 */
@Data
public class ChatGifAddDTO implements Serializable {

    @ApiModelProperty(value = "关键词")
    private String name;

    @ApiModelProperty(value = "图片地址")
    private String fileUrl;
}
