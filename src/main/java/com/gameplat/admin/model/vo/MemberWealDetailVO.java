package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lily
 * @description 会员俸禄派发详情出参
 * @date 2021/11/27
 */
@Data
public class MemberWealDetailVO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "主键")
  @TableId(type = IdType.AUTO)
  private Long id;

  @Schema(description = "福利表主键")
  private Long wealId;

  @Schema(description = "会员id")
  private Long userId;

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "会员层级")
  private Integer level;

  @Schema(description = "派发金额")
  private BigDecimal rewordAmount;

  @Schema(description = "1：未派发   2：已派发  3:已回收")
  private Integer status;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "修改人")
  private String updateBy;

  @Schema(description = "修改时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date updateTime;

  @Schema(description = "备注")
  private String remark;
}
