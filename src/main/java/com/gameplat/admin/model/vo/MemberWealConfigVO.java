package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 会员权益
 * @date 2022/1/15
 */
@Data
public class MemberWealConfigVO implements Serializable {

  @ApiModelProperty(value = "主键")
  @TableId(type = IdType.AUTO)
  private Long id;

  @ApiModelProperty(value = "权益名称")
  private String name;

  @ApiModelProperty(value = "最低专享等级")
  private Integer level;

  @ApiModelProperty(value = "未开启描述")
  private String turnDownDesc;

  @ApiModelProperty(value = "开启描述")
  private String turnUpDesc;

  @ApiModelProperty(value = "H5权益图标")
  private String image;

  @ApiModelProperty(value = "web端权益图标")
  private String webImage;

  @ApiModelProperty(value = "排序值")
  private Integer sort;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @ApiModelProperty(value = "修改人")
  private String updateB;

  @ApiModelProperty(value = "修改时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;
}
