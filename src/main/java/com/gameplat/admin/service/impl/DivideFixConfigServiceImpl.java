package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.DivideFixConfigMapper;
import com.gameplat.admin.mapper.DivideLayerConfigMapper;
import com.gameplat.admin.model.domain.proxy.DivideFixConfig;
import com.gameplat.admin.model.domain.proxy.DivideLayerConfig;
import com.gameplat.admin.service.DivideFixConfigService;
import com.gameplat.admin.service.DivideLayerConfigService;
import org.springframework.stereotype.Service;

@Service
public class DivideFixConfigServiceImpl extends ServiceImpl<DivideFixConfigMapper, DivideFixConfig> implements DivideFixConfigService {
}
