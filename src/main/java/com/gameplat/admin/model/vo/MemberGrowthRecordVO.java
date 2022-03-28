package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
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

  @ApiModelProperty(value = "主键")
  private Long id;

  @ApiModelProperty(value = "类型：0:充值  1:签到 2:投注打码量 3:后台修改 4:完善资料 5：绑定银行卡")
  private Integer type;

  @ApiModelProperty(value = "游戏平台")
  private String kindName;

  @ApiModelProperty(value = "会员账号")
  private String userName;

  @ApiModelProperty(value = "变动前的等级")
  private Integer oldLevel;

  @ApiModelProperty(value = "变动后的等级")
  private Integer currentLevel;

  @ApiModelProperty(value = "变动前的成长值")
  private Long oldGrowth;

  @ApiModelProperty(value = "变动倍数")
  private Double changeMult;

  @ApiModelProperty(value = "变动的成长值")
  private Long changeGrowth;

  @ApiModelProperty(value = "变动后的成长值")
  private Long currentGrowth;

  @ApiModelProperty(value = "创建时间")
  @JsonSerialize(using = Date2LongSerializerUtils.class)
  private Date createTime;
}
