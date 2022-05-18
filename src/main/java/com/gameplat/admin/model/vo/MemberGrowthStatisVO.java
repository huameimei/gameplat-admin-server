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
 * @description vip等级汇总查询出参
 * @date 2021/11/24
 */
@Data
public class MemberGrowthStatisVO implements Serializable {

  @Schema(description = "主键")
  @TableId(type = IdType.AUTO)
  private Long id;

  @Schema(description = "会员账号")
  private String account;

  @Schema(description = "会员当前等级")
  private Integer vipLevel;

  @Schema(description = "会员当前的成长值")
  private Integer vipGrowth;

  @Schema(description = "充值累计成长值")
  private Integer rechargeGrowth;

  @Schema(description = "签到累计成长值")
  private Integer signGrowth;

  @Schema(description = "打码量累计成长值")
  private Integer damaGrowth;

  @Schema(description = "后台修改累计成长值")
  private Integer backGrowth;

  @Schema(description = "完善资料累计成长值")
  private Integer infoGrowth;

  @Schema(description = "绑定银行卡累计成长值")
  private Integer bindGrowth;

  @Schema(description = "未达到保底累计扣除")
  private Integer demoteGrowth;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;
}
