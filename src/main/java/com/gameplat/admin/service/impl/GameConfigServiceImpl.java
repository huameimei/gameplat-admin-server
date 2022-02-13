package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.GameConfigMapper;
import com.gameplat.admin.model.domain.GameConfig;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.common.constant.CachedKeys;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameConfigServiceImpl extends ServiceImpl<GameConfigMapper, GameConfig>
    implements GameConfigService {

  @Autowired
  private GameConfigMapper gameConfigMapper;


  @Override
  @Cached(name = CachedKeys.GAME_CONFIG_CACHE, key = "#platCode", expire = 7200)
  public  GameConfig queryGameConfigInfoByPlatCode(String platCode){
    LambdaQueryWrapper<GameConfig>  queryWrapper = Wrappers.lambdaQuery();
    queryWrapper.eq(GameConfig::getPlatCode,platCode);
    GameConfig  gameConfig = gameConfigMapper.selectOne(queryWrapper);
    if (ObjectUtil.isNotNull(gameConfig)) {
        return gameConfig;
    }
    return null;
  }


}
