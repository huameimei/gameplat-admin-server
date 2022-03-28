package com.gameplat.admin.model.vo;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class DivideLayerConfigVo {

  @ApiModelProperty(value = "编号")
  private Long id;

  @ApiModelProperty(value = "代理ID")
  private Long userId;

  @ApiModelProperty(value = "代理账号")
  private String userName;

  @ApiModelProperty(value = "上级ID")
  private Long parentId;

  @ApiModelProperty(value = "上级名称")
  private String parentName;

  @ApiModelProperty(value = "代理路径")
  private String superPath;

  @ApiModelProperty(value = "级别")
  private Integer agentLevel;

  @ApiModelProperty(value = "分红配置")
  private String divideConfig;

  @ApiModelProperty(value = "上级分红分红配置")
  private String parentDivideConfig;

  @ApiModelProperty(value = "会员:M 代理：A 推广:P 试玩 :T")
  private String userType;

  @ApiModelProperty(value = "创建时间")
  private Date createTime;

  @ApiModelProperty(value = "创建人")
  private String createBy;

  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

  @ApiModelProperty(value = "更新人")
  private String updateBy;

  @ApiModelProperty(value = "下级人数")
  private Integer childNum;

  private Integer subAgent; // 总下级代理数
  private Integer subMember; // 总下级会员数
  private Integer subChildMember; // 直接下级会员数
  private Integer subChildAgent; // 直接下级代理数

  @ApiModelProperty(value = "对应的配置map")
  private Map<String, JSONObject> divideMap;
}
