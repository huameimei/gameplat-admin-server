package com.gameplat.admin.service.live.api.ae.bean;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AeResponse<T> implements Serializable {

  private String status;

  private String desc;
}
