package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author aBen
 * @date 2021/12/29 10:42
 * @desc
 */
@Data
public class CleanAccountDTO implements Serializable {

  private static final long serialVersionUID = 1599256002513411597L;

  @ApiModelProperty(value = "单个/多个会员账号(以逗号分隔)")
  private String userNames;

  @ApiModelProperty(value = "账号类型 2会员 4推广")
  private Integer userType;

  @ApiModelProperty(value = "是否清除全部 0否1是")
  private Integer isCleanAll;

  @ApiModelProperty(value = "会员账号集合")
  private List<String> userNameList;
}
