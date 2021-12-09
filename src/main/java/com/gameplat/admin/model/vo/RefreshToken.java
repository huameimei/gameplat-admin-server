package com.gameplat.admin.model.vo;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken implements Serializable {

  /** 用户token */
  private String accessToken;

  /** 用户token */
  private String refreshToken;

  /** token 过期时间 */
  private Long tokenExpireIn;
}
