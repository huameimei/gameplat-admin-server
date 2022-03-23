package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.SysTenantSettingMapper;
import com.gameplat.admin.service.SysTenantSettingService;
import com.gameplat.model.entity.sys.SysTenantSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author lily
 * @description
 * @date 2022/2/16
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class SysTenantSettingServiceImpl
    extends ServiceImpl<SysTenantSettingMapper, SysTenantSetting>
    implements SysTenantSettingService {

  @Override
  public void updateChatEnable(String cpChatEnable) {
    SysTenantSetting sportConfig = getSportConfig();
    JSONObject json = JSONObject.parseObject(sportConfig.getSettingValue());
    json.remove("cpChatEnable");
    json.put("cpChatEnable", cpChatEnable);

    sportConfig.setSettingValue(json.toJSONString());
    updateSportConfig(sportConfig);
  }

  @Override
  public SysTenantSetting getSportConfig() {
    return this.lambdaQuery().eq(SysTenantSetting::getSettingType, "sport_config").one();
  }

  @Override
  public void updateTenantSettingValue(SysTenantSetting sysTenantSetting) {
    LambdaUpdateWrapper<SysTenantSetting> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper
        .eq(SysTenantSetting::getSettingType, sysTenantSetting.getSettingType())
        .eq(SysTenantSetting::getSettingCode, sysTenantSetting.getSettingCode())
        .set(SysTenantSetting::getSettingValue, sysTenantSetting.getSettingValue())
        .set(SysTenantSetting::getUpdateTime, new Date());
    update(updateWrapper);
  }

  @Override
  public void updateSportConfig(SysTenantSetting sysTenantSetting) {
    LambdaUpdateWrapper<SysTenantSetting> updateWrapper = new LambdaUpdateWrapper<>();
    updateWrapper
        .eq(SysTenantSetting::getSettingType, "sport_config")
        .set(
            ObjectUtil.isNotEmpty(sysTenantSetting.getSettingValue()),
            SysTenantSetting::getSettingValue,
            sysTenantSetting.getSettingValue());
    update(updateWrapper);
  }
}
