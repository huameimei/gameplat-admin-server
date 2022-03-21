package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.model.vo.TenantSettingVO;
import com.gameplat.model.entity.setting.TenantSetting;
import feign.Param;

import java.util.List;

/**
 * @author martin
 * @description
 * @date 2022/3/10
 */
public interface TenantSettingMapper extends BaseMapper<TenantSetting> {

  /**
   * 给租户初使化对应主题的导航栏
   *
   * @param theme
   * @param settingType
   */
  int initTenantNavigation(String theme, String settingType);

  /** 查询配置信息 */
  List<TenantSetting> getBackendAppNavigationList(TenantSettingVO tenantSettingVO);

  /**
   * 插入配置信息
   *
   * @param oneCode
   * @return
   */
  GameKindVO getGameList(@Param("oneCode") String oneCode);

  void updateIndex(TenantSetting tenantSetting1);
}
