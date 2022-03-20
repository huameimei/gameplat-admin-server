package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.sys.SysTenantSetting;

public interface SysTenantSettingService extends IService<SysTenantSetting> {

  void updateSportConfig(SysTenantSetting sysTenantSetting);

  /** 修改聊天室开关 */
  void updateChatEnable(String cpChatEnable);

  /** 查询配置信息 */
  SysTenantSetting getSportConfig();

  /**
   * 修改租户设置的值
   *
   * @param sysTenantSetting
   * @return
   */
  void updateTenantSettingValue(SysTenantSetting sysTenantSetting);
}
