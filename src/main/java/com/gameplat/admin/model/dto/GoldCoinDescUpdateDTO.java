package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/3/2
 */
@Data
public class GoldCoinDescUpdateDTO implements Serializable {

  @Schema(description = "id")
  private Integer id;

  @Schema(description = "金币描述")
  private String goldCoinDesc;
}
