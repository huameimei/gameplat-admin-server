package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.setting.TenantSetting;

/**
 * @author martin
 * @description
 * @date 2022/3/10
 */
public interface TenantSettingMapper extends BaseMapper<TenantSetting> {

    /**
     * 给租户初使化对应主题的导航栏
     * @param theme
     * @param settingType
     */
    int initTenantNavigation(String theme, String settingType);
}
