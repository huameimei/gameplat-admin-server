package com.gameplat.admin.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer code;

  private String message;
}
