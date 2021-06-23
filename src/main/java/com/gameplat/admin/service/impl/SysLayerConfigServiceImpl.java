package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.dao.SysLayerConfigMapper;
import com.gameplat.admin.model.entity.SysLayerConfig;
import com.gameplat.admin.service.SysLayerConfigService;
import org.springframework.stereotype.Service;

@Service
public class SysLayerConfigServiceImpl extends
    ServiceImpl<SysLayerConfigMapper, SysLayerConfig> implements SysLayerConfigService {

}
