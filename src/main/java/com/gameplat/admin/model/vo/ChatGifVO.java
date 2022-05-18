package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/13
 */
@Data
public class ChatGifVO implements Serializable {

  @Schema(description = "主键")
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  @Schema(description = "关键词")
  private String name;

  @Schema(description = "图片地址")
  private String fileUrl;
}
