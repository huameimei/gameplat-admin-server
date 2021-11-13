package com.gameplat.admin.model.dto;

import com.gameplat.common.model.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ip白名单DTO
 *
 * @author three
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AuthIpDTO extends BaseDTO {

  private String ip;

  private String ipType;

  private String remark;
}
