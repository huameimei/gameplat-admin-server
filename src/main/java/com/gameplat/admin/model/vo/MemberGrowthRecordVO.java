package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 查询vip成长记录出参
 * @date 2021/11/23
 */
@Data
public class MemberGrowthRecordVO implements Serializable {

  @Schema(description = "主键")
  private Long id;

  @Schema(description = "类型：0:充值  1:签到 2:投注打码量 3:后台修改 4:完善资料 5：绑定银行卡")
  private Integer type;

  @Schema(description = "游戏平台")
  private String kindName;

  @Schema(description = "会员账号")
  private String userName;

  @Schema(description = "变动前的等级")
  private Integer oldLevel;

  @Schema(description = "变动后的等级")
  private Integer currentLevel;

  @Schema(description = "变动前的成长值")
  private Long oldGrowth;

  @Schema(description = "变动倍数")
  private Double changeMult;

  @Schema(description = "变动的成长值")
  private Long changeGrowth;

  @Schema(description = "变动后的成长值")
  private Long currentGrowth;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;
}
