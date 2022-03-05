package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/2/13
 */
@Data
public class ChatGifVO implements Serializable {

  @ApiModelProperty(value = "主键")
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  @ApiModelProperty(value = "关键词")
  private String name;

  @ApiModelProperty(value = "图片地址")
  private String fileUrl;
}
