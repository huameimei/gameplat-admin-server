package com.gameplat.admin.model.dto;

import com.gameplat.admin.model.vo.FissionConfigLevelVo;
import com.gameplat.admin.model.vo.FissionDivideConfigVo;
import com.gameplat.admin.model.vo.GameDivideVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DivideConfigDTO {

  @Schema(description = "修改的分红参数")
  Map<String, List<GameDivideVo>> ownerConfigMap;

  @Schema(description = "修改的分红参数")
  Map<String, List<GameDivideVo>> ownerFixConfigMap;

  @Schema(description = "修改的裂变模式分红map参数")
  Map<String, List<FissionDivideConfigVo>> ownerFissionConfigMap;

  @Schema(description = "编号")
  private Long id;

  @Schema(description = "代理ID")
  private Long userId;

  @Schema(description = "代理账号")
  private String userName;

  @Schema(description = "代理账号")
  private String agentName;

  @Schema(description = "上级账号")
  private String parentName;

  @Schema(description = "分红配置")
  private String divideConfig;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新时间")
  private Date updateTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "是否只查询下级")
  private Integer isOnlyQueryChild;

  @Schema(description = "查询下级属性  1 只查直属下级  2 查询所有下级")
  private Integer queryChild;

  @Schema(description = "裂变模式周期配置")
  private List<FissionConfigLevelVo> fissionConfigLevelVos = new ArrayList<>();

  @Schema(description = "周期配置")
  private String recycleConfig;

  @Schema(description = "周期外分红点")
  private BigDecimal outRecycleConfig;
}
