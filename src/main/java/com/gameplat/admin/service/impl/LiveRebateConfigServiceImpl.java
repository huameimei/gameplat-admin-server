package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.LiveRebateConfigConvert;
import com.gameplat.admin.mapper.LiveRebateConfigMapper;
import com.gameplat.admin.model.domain.LiveRebateConfig;
import com.gameplat.admin.model.dto.OperLiveRebateConfigDTO;
import com.gameplat.admin.service.LiveRebateConfigService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.json.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LiveRebateConfigServiceImpl
    extends ServiceImpl<LiveRebateConfigMapper, LiveRebateConfig>
    implements LiveRebateConfigService {

  @Autowired LiveRebateConfigMapper liveRebateConfigMapper;

  @Autowired LiveRebateConfigConvert liveRebateConfigConvert;

  @Override
  public List<LiveRebateConfig> queryAll(String userLevel) {
    return this.lambdaQuery()
        .eq(ObjectUtils.isNotEmpty(userLevel), LiveRebateConfig::getUserLevel, userLevel)
        .list();
  }

  @Override
  public LiveRebateConfig getById(Long id) {
    return liveRebateConfigMapper.selectById(id);
  }

  @Override
  public void addLiveRebateConfig(OperLiveRebateConfigDTO dto) {
    LiveRebateConfig liveRebateConfig = liveRebateConfigConvert.toEntity(dto);
    if (this.countByMoney(liveRebateConfig) > 0) {
      throw new ServiceException("该金额阈值已配置，请勿重复配置！");
    }
    verifyRebate(liveRebateConfig);
    if (!this.save(liveRebateConfig)) {
      throw new ServiceException("新增真人返点配置失败!");
    }
  }

  @Override
  public void updateLiveRebateConfig(OperLiveRebateConfigDTO dto) {
    LiveRebateConfig liveRebateConfig = liveRebateConfigConvert.toEntity(dto);
    if (this.countByIdNotAndMoney(liveRebateConfig) > 0) {
      throw new ServiceException("该金额阈值已配置，请勿重复配置！");
    }
    verifyRebate(liveRebateConfig);
    if (!this.updateById(liveRebateConfig)) {
      throw new ServiceException("更新真人返点配置失败!");
    }
  }

  @Override
  public void delete(Long id) {
    if (!this.removeById(id)) {
      throw new ServiceException("删除真人返点配置失败!");
    }
  }

  private int countByMoney(LiveRebateConfig liveRebateConfig) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(liveRebateConfig.getMoney()),
            LiveRebateConfig::getMoney,
            liveRebateConfig.getMoney())
        .eq(
            ObjectUtils.isNotEmpty(liveRebateConfig.getUserLevel()),
            LiveRebateConfig::getUserLevel,
            liveRebateConfig.getUserLevel())
        .count();
  }

  private int countByIdNotAndMoney(LiveRebateConfig liveRebateConfig) {
    return this.lambdaQuery()
        .eq(
            ObjectUtils.isNotEmpty(liveRebateConfig.getMoney()),
            LiveRebateConfig::getMoney,
            liveRebateConfig.getMoney())
        .eq(
            ObjectUtils.isNotEmpty(liveRebateConfig.getUserLevel()),
            LiveRebateConfig::getUserLevel,
            liveRebateConfig.getUserLevel())
        .ne(
            ObjectUtils.isNotEmpty(liveRebateConfig.getId()),
            LiveRebateConfig::getId,
            liveRebateConfig.getId())
        .count();
  }

  private void verifyRebate(LiveRebateConfig liveRebateConfig) {
    String json = liveRebateConfig.getJson();
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
