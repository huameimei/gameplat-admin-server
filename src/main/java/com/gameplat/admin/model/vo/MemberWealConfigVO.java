package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "主键")
  @TableId(type = IdType.AUTO)
  private Long id;

  @Schema(description = "权益名称")
  private String name;

  @Schema(description = "最低专享等级")
  private Integer level;

  @Schema(description = "未开启描述")
  private String turnDownDesc;

  @Schema(description = "开启描述")
  private String turnUpDesc;

  @Schema(description = "H5权益图标")
  private String image;

  @Schema(description = "web端权益图标")
  private String webImage;

  @Schema(description = "排序值")
  private Integer sort;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "修改人")
  private String updateB;

  @Schema(description = "修改时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;
}
