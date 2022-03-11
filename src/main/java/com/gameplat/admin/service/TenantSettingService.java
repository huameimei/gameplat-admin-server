package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.TenantSettingVO;
import com.gameplat.model.entity.setting.TenantSetting;

import java.util.List;

/**
 * @author martin
 * @date 2022/03/10
 * @desc
 */
public interface TenantSettingService extends IService<TenantSetting> {

    /**
     * 获取租户设置列表
     * @param query
     * @return
     */
    List<TenantSetting> getTenantSetting(TenantSettingVO query);

    /**
     * 判断租户主题是否存在
     * @param theme
     * @return
     */
    boolean isExistTenantTheme(String theme);

    /**
     * 为租户初使化对应主题的导航栏
     * @param theme
     * @return
     */
    int initTenantNavigation(String theme, String settingType);

}
