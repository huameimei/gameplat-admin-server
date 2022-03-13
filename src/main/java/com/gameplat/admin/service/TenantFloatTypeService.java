package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.setting.TenantFloatSetting;
import com.gameplat.model.entity.setting.TenantFloatType;

import java.util.List;

public interface TenantFloatTypeService extends IService<TenantFloatType> {

  /**
   * 查询游戏浮窗类型列表
   *
   * @param tenantFloatTypeVo 游戏浮窗类型
   * @return 游戏浮窗类型集合
   */
  //List<TenantFloatTypeVo> selectSysFloatTypeList(TenantFloatTypeVo tenantFloatTypeVo);

  /**
   * 新增游戏浮窗类型
   *
   * @param tenantFloatSetting 游戏浮窗类型
   * @return 结果
   */
  void insertSysFloat(TenantFloatSetting tenantFloatSetting);

  /**
   * 删除游戏浮窗类型信息
   *
   * @param id 游戏浮窗类型ID
   * @return 结果
   */
  void deleteSysFloatById(Integer id);

  void updateFloat(TenantFloatSetting tenantFloatSetting);

  void updateBatch(List<TenantFloatSetting> tenantFloatSettings);

  void updateFloatType(TenantFloatType tenantFloatType);

  void updateShowPosition(String showPositions);
}
