package com.gameplat.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameConfigMapper;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.CachedKeys;
import com.gameplat.model.entity.game.GameConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameConfigServiceImpl extends ServiceImpl<GameConfigMapper, GameConfig>
    implements GameConfigService {

  @Autowired private GameConfigMapper gameConfigMapper;

  @Override
  @Cached(name = CachedKeys.GAME_CONFIG_CACHE, key = "#platCode", expire = 7200)
  public JSONObject queryGameConfigInfoByPlatCode(String platCode) {
    LambdaQueryWrapper<GameConfig> queryWrapper = Wrappers.lambdaQuery();
    queryWrapper.eq(GameConfig::getPlatformCode, platCode);
    GameConfig gameConfig = gameConfigMapper.selectOne(queryWrapper);
    return JSONObject.parseObject(gameConfig.getConfig());
  }

  @Override
  public JSONObject getGameConfig(String platCode) {
    JSONObject jsonObject;
    GameConfig gameConfig = this.lambdaQuery().eq(GameConfig::getPlatformCode, platCode).one();
    if (StringUtils.isNotNull(gameConfig)) {
      jsonObject = JSONObject.parseObject(gameConfig.getConfig());
      jsonObject.put("currency", gameConfig.getCurrency());
    } else {
      return null;
    }
    return jsonObject;
  }
}
