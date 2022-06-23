package com.gameplat.admin.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "单个/多个会员账号(以逗号分隔)")
  private String userNames;

  @Schema(description = "账号类型 2会员 4推广")
  private Integer userType;

  @Schema(description = "是否清除全部 0否1是")
  private Integer isCleanAll;

  @Schema(description = "会员账号集合")
  private List<String> userNameList;
}
