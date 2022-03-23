package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.TenantSettingVO;
import com.gameplat.model.entity.setting.TenantSetting;

import java.util.List;

/**
 * @author martin
 * @date 2022/03/10
 */
public interface TenantSettingService extends IService<TenantSetting> {

  /**
   * 获取租户设置列表
   *
   * @param query TenantSettingVO
   * @return List
   */
  List<TenantSetting> getTenantSetting(TenantSettingVO query);

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
   * @param theme String
   * @param settingType String
   * @return int
   */
  int initTenantNavigation(String theme, String settingType);

  /**
   * 启动图配置列表
   *
   * @param page IPage
   * @param tenantSetting TenantSetting
   * @return IPage
   */
  IPage<TenantSetting> getStartImagePage(IPage<TenantSetting> page, TenantSetting tenantSetting);

  /**
   * 启动图配置新增/修改
   *
   * @param tenantSetting TenantSetting
   */
  void insertStartImagePage(TenantSetting tenantSetting);

  /**
   * 启动图配置删除
   *
   * @param tenantSetting TenantSetting
   */
  void deleteStartImagePage(TenantSetting tenantSetting);

  /**
   * 查询租户主题
   *
   * @param setting TenantSetting
   * @return List
   */
  List<TenantSetting> getTenantSetting(TenantSetting setting);

  /**
   * 获取租户设置信息
   *
   * @param vo TenantSettingVO
   * @return List
   */
  List<TenantSetting> getAppNavigation(TenantSettingVO vo);

  /**
   * 修改显示与排序
   *
   * @param vo TenantSettingVO
   */
  void updateAppNavigation(TenantSettingVO vo);

  /**
   * 批量修改排序
   *
   * @param tenantSettings List
   */
  void updateBatchTenantSetting(List<TenantSetting> tenantSettings);

  /**
   * 删除游戏浮窗类型信息
   *
   * @param id 游戏浮窗类型ID
   */
  void deleteSysFloatById(Integer id);
}
