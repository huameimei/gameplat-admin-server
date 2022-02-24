package com.gameplat.admin.model.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@TableName("game_config")
@Builder
@AllArgsConstructor
public class GameConfig implements Serializable {

  private Long id;
  /**
   * 游戏编码
   */
  private String platCode;

  /**
   * 游戏配置 json
   */
  private String config;

  /**
   * 租户标识
   */
  private String tenantCode;

  /**
   * 三方代理编码
   */
  private String agentCode;

  /**
   * 币种
   */
  private String currency;

  /**
   * 开关 0关 1开
   */
  private Integer isOpen;

  /**
   * 备注
   */
  private String remark;
}
