package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysLayerConfigMapper;
import com.gameplat.admin.service.SysLayerConfigService;
import com.gameplat.model.entity.sys.SysLayerConfig;
import org.springframework.stereotype.Service;

/**
 * @author lily
 * @description
 * @date 2021/11/28
 */
@Service
public class SysLayerConfigServiceImpl extends ServiceImpl<SysLayerConfigMapper, SysLayerConfig>
    implements SysLayerConfigService {

  @Override
  public SysLayerConfig findUserLevel(Integer layerValue) {
    return this.lambdaQuery().eq(SysLayerConfig::getLayerValue, layerValue).one();
  }
}
