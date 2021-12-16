package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GameConfig;
import java.util.Map;

public interface GameConfigService extends IService<GameConfig> {

  Map<String,String> queryGameConfigInfoByPlatCode(String platCode);
}
