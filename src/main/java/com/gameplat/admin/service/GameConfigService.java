package com.gameplat.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GameConfig;
import java.util.Map;

public interface GameConfigService extends IService<GameConfig> {

  JSONObject queryGameConfigInfoByPlatCode(String platCode);
}
