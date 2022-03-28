package com.gameplat.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLowerNumDTO {

  private String account;

  private Integer lowerNum;
}
