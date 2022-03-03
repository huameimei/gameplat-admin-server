package com.gameplat.admin.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.gameplat.admin.model.vo.FissionDivideConfigVo;
import com.gameplat.admin.model.vo.FissionDivideLevelVo;
import com.gameplat.admin.model.vo.GameDivideVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cc
 * @date 2021/12/13 18:55
 * @desc 代理分红限制配置
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendConfigDto implements Serializable {

  @ApiModelProperty(value = "修改的分红参数")
  Map<String, List<GameDivideVo>> ownerConfigMap;
  @ApiModelProperty(value = "修改的固定比例模式分红参数")
  Map<String, List<GameDivideVo>> ownerFixConfigMap;
  @ApiModelProperty(value = "修改的裂变模式分红map参数")
  Map<String, List<FissionDivideConfigVo>> ownerFissionConfigMap;
  @TableId(type = IdType.AUTO)
  private Long id;
  @ApiModelProperty(value = "是否开启代理中心 1 开启  0 关闭")
  @Range(min = 0, max = 1, message = "只能未0过1")
  private Integer isOpenAgentCenter;
  @ApiModelProperty(value = "分红是否累计 1 累计 0 不累计")
  @Range(min = 0, max = 1, message = "只能未0过1")
  private Integer isGrand;
  @ApiModelProperty(value = "是否包含代理数据")
  @Range(min = 0, max = 1, message = "只能未0过1")
  private Integer isIncludeAgent;
  @ApiModelProperty(value = "是否查询直属下级  1 是  0 否")
  @Range(min = 0, max = 1, message = "只能未0过1")
  private Integer isDirect;
  @ApiModelProperty(value = "全名注册代理开关")
  @Range(min = 0, max = 1, message = "只能未0过1")
  private Integer allRegisterAgent;
  @ApiModelProperty(value = "分红模式：分红模式 1 固定模式  2 裂变模式 3 层层代模式 4 平级模式")
  @Range(min = 0, max = 4, message = "只能1-4的整数")
  private Integer divideModel;
  @ApiModelProperty(value = "全民注册代理开启后 是否开启固定模式分红配置预设")
  @Range(min = 0, max = 1, message = "只能未0过1")
  private Integer fixDevideIsPreset;
  @ApiModelProperty(value = "固定模式 预设值")
  private String fixPresetValue;
  @ApiModelProperty(value = "全民注册代理开启后 是否开启裂变模式分红配置预设")
  @Range(min = 0, max = 1, message = "只能未0过1")
  private Integer fissionDevideIsPreset;
  @ApiModelProperty(value = "裂变模式配置")
  private String fissionPresetValue;
  @ApiModelProperty(value = "裂变模式 周期预设配置")
  private String recyclePresetValue;
  @ApiModelProperty(value = "裂变模式 周期外比例")
  private BigDecimal outRecyclePresetValue;
  @ApiModelProperty(value = "全民注册代理开启后 是否开启层层代理分红配置预设")
  @Range(min = 0, max = 1, message = "只能未0过1")
  private Integer layerDivideIsPreset;
  @ApiModelProperty(value = "层层代模式 预设值")
  private String layerPresetValue;
  @ApiModelProperty(value = "是否开启打码量计算")
  @Range(min = 0, max = 1, message = "只能未0过1")
  private Integer isOpenValidWithdraw;
  @ApiModelProperty(value = "开启打码量计算后的倍数")
  private BigDecimal validWithdrawMult;
  @ApiModelProperty(value = "有效会员定义 最低投注额")
  private BigDecimal validAmountLimit;
  @ApiModelProperty(value = "有效会员定义 最低充值额")
  private BigDecimal rechargeAmountLimit;
  @ApiModelProperty(value = "是否开启代理后台敏感字段")
  @Range(min = 0, max = 1, message = "只能未0过1")
  private Integer isOpenSensitive;
  @ApiModelProperty(value = "代理后台名字段")
  private String sensitiveColumn;
  private List<FissionDivideLevelVo> fissionConfigLevelVos = new ArrayList<>();
}
