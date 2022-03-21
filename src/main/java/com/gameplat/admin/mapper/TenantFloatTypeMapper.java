package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.vo.TenantFloatTypeVo;
import com.gameplat.model.entity.setting.TenantFloatSetting;
import com.gameplat.model.entity.setting.TenantFloatType;

import java.util.List;

public interface TenantFloatTypeMapper extends BaseMapper<TenantFloatType> {
  /**
   * 查询游戏浮窗类型列表
   *
   * @param tenantFloatTypeVo 游戏浮窗类型
   * @return 游戏浮窗类型集合
   */
  List<TenantFloatTypeVo> selectSysFloatTypeList(TenantFloatTypeVo tenantFloatTypeVo);

  /**
   * 新增游戏浮窗
   *
   * @param tenantFloatSetting 游戏浮窗
   * @return 结果
   */
  void insertSysFloatSetting(TenantFloatSetting tenantFloatSetting);

  /**
   * 修改游戏浮窗
   *
   * @param tenantFloatSetting 游戏浮窗
   * @return 结果
   */
  void updateSysFloat(TenantFloatSetting tenantFloatSetting);

  void updateBatch(List<TenantFloatSetting> sysFloatSettings);

  void updateShowPosition(String showPositions);
}
