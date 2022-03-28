package com.gameplat.admin.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/3/2
 */
@Data
public class GoldCoinDescUpdateDTO implements Serializable {

  @ApiModelProperty("id")
  private Integer id;

  @ApiModelProperty("金币描述")
  private String goldCoinDesc;
}
