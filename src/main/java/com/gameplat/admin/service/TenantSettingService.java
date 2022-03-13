package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
     * @param settingType
     * @return
     */
    int initTenantNavigation(String theme, String settingType);

    /**
     * 启动图配置列表
     * @param page
     * @param tenantSetting
     * @return
     */
    IPage<TenantSetting> getStartImagePage(IPage<TenantSetting> page, TenantSetting tenantSetting);

    /**
     * 启动图配置新增/修改
     * @param tenantSetting
     */
    void insertStartImagePage(TenantSetting tenantSetting);

    /**
     * 启动图配置删除
     * @param tenantSetting
     */
    void deleteStartImagePage(TenantSetting tenantSetting);

    /**
     * 查询租户主题
     * @param setting
     * @return
     */
    List<TenantSetting> getTenantSetting(TenantSetting setting);

    /**
     * 获取租户设置信息
     * @param tenantSetting
     * @return
     */
    List<TenantSetting> getAppNavigation(TenantSetting tenantSetting);

    /**
     * 修改显示与排序
     *
     * @param tenantSettingVO
     */
    void updateAppNavigation(TenantSettingVO tenantSettingVO);

    /**
     * 批量修改排序
     * @param tenantSettings
     */
    void updateBatchTenantSetting(List<TenantSetting> tenantSettings);

    /**
     * 删除游戏浮窗类型信息
     *
     * @param id 游戏浮窗类型ID
     * @return 结果
     */
    void deleteSysFloatById(Integer id);
}
