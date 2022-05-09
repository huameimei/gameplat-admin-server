package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.model.vo.SysSettingVO;
import com.gameplat.model.entity.setting.SysSetting;
import feign.Param;

import java.util.List;

public interface SysSettingMapper extends BaseMapper<SysSetting> {

    /**
     * 给租户初使化对应主题的导航栏
     * @param theme
     * @param settingType
     */
    int initTenantNavigation(String theme, String settingType);

    /**
     * 查询配置信息
     */
    List<SysSetting> getBackendAppNavigationList(SysSettingVO tenantSettingVO);

    /**
     * 插入配置信息
     * @param oneCode
     * @return
     */
    GameKindVO getGameList(@Param("oneCode") String oneCode);

    void updateIndex(SysSetting sysSetting1);

    /**
     * 插入配置信息
     */
    void insetGameList(List<SysSetting> sysSettings);

    /**
     * 查询配置信息
     */
    SysSetting getSportConfig();

    /**
     * 初始化体育配置
     *
     * @param sysSetting
     * @return
     */
    int initSportConfig(SysSetting sysSetting);

    /**
     * 修改体育配置排序开关列表
     *
     * @param sysSetting
     * @return
     */
    int updateListSortConfig(SysSetting sysSetting);

    /**
     * 多条件查询租户设置列表
     *
     * @param sysSetting
     * @return
     */
    List<SysSetting> getTenantSetting(SysSetting sysSetting);

    /**
     * 修改体育配置
     *
     * @param sysSysSetting
     * @return
     */
    int updateSportConfig(SysSetting sysSysSetting);
}
