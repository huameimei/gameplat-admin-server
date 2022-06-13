package com.gameplat.admin.model.vo;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class DivideLayerConfigVo {

  @Schema(description = "编号")
  private Long id;

  @Schema(description = "代理ID")
  private Long userId;

  @Schema(description = "代理账号")
  private String userName;

  @Schema(description = "上级ID")
  private Long parentId;

  @Schema(description = "上级名称")
  private String parentName;

  @Schema(description = "代理路径")
  private String superPath;

  @Schema(description = "级别")
  private Integer agentLevel;

  @Schema(description = "分红配置")
  private String divideConfig;

  @Schema(description = "上级分红分红配置")
  private String parentDivideConfig;

  @Schema(description = "会员:M 代理：A 推广:P 试玩 :T")
  private String userType;

  @Schema(description = "创建时间")
  private Date createTime;

  @Schema(description = "创建人")
  private String createBy;

  @Schema(description = "更新时间")
  private Date updateTime;

  @Schema(description = "更新人")
  private String updateBy;

  @Schema(description = "下级人数")
  private Integer childNum;

  private Integer subAgent; // 总下级代理数
  private Integer subMember; // 总下级会员数
  private Integer subChildMember; // 直接下级会员数
  private Integer subChildAgent; // 直接下级代理数

  @Schema(description = "对应的配置map")
  private Map<String, JSONObject> divideMap;
}
