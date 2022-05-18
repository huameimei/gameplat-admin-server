package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/13
 */
@Data
public class ChatGifAddDTO implements Serializable {

  @Schema(description = "关键词")
  private String name;

  @Schema(description = "图片地址")
  private String fileUrl;
}
