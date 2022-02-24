package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.DivideFissionConfigMapper;
import com.gameplat.admin.mapper.DivideFixConfigMapper;
import com.gameplat.admin.model.domain.proxy.DivideFissionConfig;
import com.gameplat.admin.model.domain.proxy.DivideFixConfig;
import com.gameplat.admin.service.DivideFissionConfigService;
import com.gameplat.admin.service.DivideFixConfigService;
import org.springframework.stereotype.Service;

@Service
public class DivideFissionConfigServiceImpl extends ServiceImpl<DivideFissionConfigMapper, DivideFissionConfig> implements DivideFissionConfigService {
}
