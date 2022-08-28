package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.ListSortConfigVO;
import com.gameplat.admin.model.vo.SportConfigVO;
import com.gameplat.admin.model.vo.SportConfigValueVO;
import com.gameplat.admin.model.vo.SysSettingVO;
import com.gameplat.model.entity.setting.SysSetting;

import java.util.List;

public interface SysSettingService extends IService<SysSetting> {

    void updateSportConfig(SysSetting sysSetting);

    /**
     * 修改聊天室开关
     */
    void updateChatEnable(String cpChatEnable);

    /**
     * 查询配置信息
     */
    SysSetting getSportConfigSetting();

    /**
     * 获取租户设置列表
     *
     * @param query TenantSettingVO
     * @return List
     */
    List<SysSetting> getTenantSetting(SysSettingVO query);

    /**
     * 判断租户主题是否存在
     *
     * @param theme String
     * @return boolean
     */
    boolean isExistTenantTheme(String theme);

    /**
     * 为租户初使化对应主题的导航栏
     *
     * @param theme       String
     * @param settingType String
     * @return int
     */
    int initTenantNavigation(String theme, String settingType);

    /**
     * 启动图配置列表
     *
     * @param page       IPage
     * @param sysSetting TenantSetting
     * @return IPage
     */
    IPage<SysSetting> getStartImagePage(IPage<SysSetting> page, SysSetting sysSetting);

    /**
     * 启动图配置新增/修改
     *
     * @param sysSetting TenantSetting
     */
    void insertStartImagePage(SysSetting sysSetting);

    /**
     * 启动图配置删除
     *
     * @param sysSetting TenantSetting
     */
    void deleteStartImagePage(SysSetting sysSetting);

    /**
     * 查询租户主题
     *
     * @param setting TenantSetting
     * @return List
     */
    List<SysSetting> getTenantSetting(SysSetting setting);

    /**
     * 获取租户设置信息
     *
     * @param vo TenantSettingVO
     * @return List
     */
    List<SysSetting> getAppNavigation(SysSettingVO vo);

    /**
     * 修改显示与排序
     *
     * @param vo TenantSettingVO
     */
    void updateAppNavigation(SysSettingVO vo);

    /**
     * 批量修改排序
     * @param sysSettings List
     */
    void updateBatchTenantSetting(List<SysSetting> sysSettings);

    /**
     * 删除游戏浮窗类型信息
     * @param id 游戏浮窗类型ID
     */
    void deleteSysFloatById(Integer id);

    /**
     * 修改租户设置的值
     * @param tenantSetting
     * @return
     */
    void updateTenantSettingValue(SysSettingVO tenantSetting);

    /**
     * 获取体育配置
     *
     * @return
     */
    SportConfigValueVO getSportConfig();

    /**
     * 修改体育配置排序开关列表
     * @param listSortConfigVOS
     * @return
     */
    int updateListSortConfig(List<ListSortConfigVO> listSortConfigVOS);

    /**
     * 修改体育配置
     */
    int updateSportConfig(SportConfigVO sportConfigVo);

    /**
     * 初始化注单菜单
     * @return
     */
    List<SysSetting> initBettingMenu();
}
