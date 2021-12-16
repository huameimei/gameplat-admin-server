package com.gameplat.admin.model.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@TableName("plt_game_config")
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
   * 币种
   */
  private String currency;

  /**
   * 三方代理编号  游戏平台_三方代理号
   */
  private String agentId;

  /**
   * 租户标识
   */
  private String tenantCode;
}
