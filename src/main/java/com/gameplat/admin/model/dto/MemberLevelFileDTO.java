package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aBen
 * @date 2022/3/5 18:41
 * @desc
 */
@Data
public class MemberLevelFileDTO implements Serializable {

  @Schema(description = "会员账号")
  private String account;
}
