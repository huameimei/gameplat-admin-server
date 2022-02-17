package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SysTenantSetting;

public interface SysTenantSettingService extends IService<SysTenantSetting> {

    void updateSportConfig(SysTenantSetting sysTenantSetting);

    /** 修改聊天室开关 */
    void updateChatEnable(String cpChatEnable);

    /** 查询配置信息 */
    SysTenantSetting getSportConfig();
}
