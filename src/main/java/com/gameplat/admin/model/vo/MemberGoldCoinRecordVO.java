package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.common.util.Date2LongSerializerUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 金币
 * @date 2022/3/1
 */
@Data
public class MemberGoldCoinRecordVO implements Serializable {

  private Integer id;

  @Schema(description = "来源类型（待定）1 获取成长值、2 爆料扣款")
  private Integer sourceType;

  @Schema(description = "账号")
  private String account;

  @Schema(description = "变动前金币数")
  private Integer beforeBalance;

  @Schema(description = "变动金币数")
  private Integer amount;

  @Schema(description = "变动后金币数")
  private Integer afterBalance;

  @Schema(description = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;

  @Schema(description = "备注")
  private String remark;
}
