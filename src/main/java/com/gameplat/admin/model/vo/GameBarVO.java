package com.gameplat.admin.model.vo;

import com.gameplat.model.entity.game.GameBar;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏导航栏VO
 */
@Data
public class GameBarVO extends GameBar  implements Serializable  {


  /**
   * 底部游戏
   */
  private List<GameBar> gameBarList = new ArrayList<>();
}
