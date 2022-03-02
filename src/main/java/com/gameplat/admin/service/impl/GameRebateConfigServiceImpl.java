package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameRebateConfigConvert;
import com.gameplat.admin.mapper.GameRebateConfigMapper;
import com.gameplat.admin.model.dto.OperGameRebateConfigDTO;
import com.gameplat.admin.service.GameRebateConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.model.entity.game.GameRebateConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameRebateConfigServiceImpl
    extends ServiceImpl<GameRebateConfigMapper, GameRebateConfig>
    implements GameRebateConfigService {

  @Autowired GameRebateConfigMapper gameRebateConfigMapper;

  @Autowired GameRebateConfigConvert gameRebateConfigConvert;

  @Override
  public List<GameRebateConfig> queryAll(String userLevel) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(userLevel), GameRebateConfig::getUserLevel, userLevel)
        .list();
  }

  @Override
  public GameRebateConfig getById(Long id) {
    return gameRebateConfigMapper.selectById(id);
  }

  @Override
  public void addGameRebateConfig(OperGameRebateConfigDTO dto) {
    GameRebateConfig gameRebateConfig = gameRebateConfigConvert.toEntity(dto);
    if (this.countByMoney(gameRebateConfig) > 0) {
      throw new ServiceException("该金额阈值已配置，请勿重复配置！");
    }
    verifyRebate(gameRebateConfig);
    if (!this.save(gameRebateConfig)) {
      throw new ServiceException("新增真人返点配置失败!");
    }
  }

  @Override
  public void updateGameRebateConfig(OperGameRebateConfigDTO dto) {
    GameRebateConfig gameRebateConfig = gameRebateConfigConvert.toEntity(dto);
    if (this.countByIdNotAndMoney(gameRebateConfig) > 0) {
      throw new ServiceException("该金额阈值已配置，请勿重复配置！");
    }
    verifyRebate(gameRebateConfig);
    if (!this.updateById(gameRebateConfig)) {
      throw new ServiceException("更新真人返点配置失败!");
    }
  }

  @Override
  public void delete(Long id) {
    if (!this.removeById(id)) {
      throw new ServiceException("删除真人返点配置失败!");
    }
  }

  private Long countByMoney(GameRebateConfig gameRebateConfig) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(gameRebateConfig.getMoney()),
            GameRebateConfig::getMoney,
            gameRebateConfig.getMoney())
        .eq(
            ObjectUtils.isNotEmpty(gameRebateConfig.getUserLevel()),
            GameRebateConfig::getUserLevel,
            gameRebateConfig.getUserLevel())
        .count();
  }

  private Long countByIdNotAndMoney(GameRebateConfig gameRebateConfig) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(gameRebateConfig.getMoney()),
            GameRebateConfig::getMoney,
            gameRebateConfig.getMoney())
        .eq(
            ObjectUtils.isNotEmpty(gameRebateConfig.getUserLevel()),
            GameRebateConfig::getUserLevel,
            gameRebateConfig.getUserLevel())
        .ne(
            ObjectUtils.isNotEmpty(gameRebateConfig.getId()),
            GameRebateConfig::getId,
            gameRebateConfig.getId())
        .count();
  }

  private void verifyRebate(GameRebateConfig gameRebateConfig) {
    String json = gameRebateConfig.getJson();
    if (StringUtils.isEmpty(json)) {
      return;
    }
    List<Map> list = JsonUtils.parse(json, List.class);
    if (list != null
        && list.stream()
            .map(m -> (Number) m.get("rebate"))
            .anyMatch(r -> r != null && r.doubleValue() < 0)) {
      throw new ServiceException("返点不能小于0");
    }
  }
}
