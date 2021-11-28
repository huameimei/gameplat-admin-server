package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SysLayerConfig;

/**
 * @author lily
 * @description
 * @date 2021/11/28
 */

public interface SysLayerConfigService extends IService<SysLayerConfig> {

    SysLayerConfig findUserLevel(Integer layerValue);
}
