package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * banner信息DTO
 *
 * @author admin
 */
@Data
public class SysBannerInfoQueryDTO implements Serializable {

  /** 语种 */
  @Schema(description = "语种")
  private String language;
}
