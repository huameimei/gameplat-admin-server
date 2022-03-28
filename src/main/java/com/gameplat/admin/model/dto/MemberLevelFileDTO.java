package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aBen
 * @date 2022/3/5 18:41
 * @desc
 */
@Data
public class MemberLevelFileDTO implements Serializable {

  @ApiModelProperty(value = "会员账号")
  private String account;
}
