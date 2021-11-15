package com.gameplat.admin.model.bean;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer code;

  private String message;

}
