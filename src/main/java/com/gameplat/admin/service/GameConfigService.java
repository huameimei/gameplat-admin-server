package com.gameplat.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.game.GameConfig;

public interface GameConfigService extends IService<GameConfig> {

  JSONObject queryGameConfigInfoByPlatCode(String platCode);

  JSONObject getGameConfig(String platCode);
}
