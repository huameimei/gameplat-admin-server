package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.SysFloatTypeVo;
import com.gameplat.model.entity.setting.SysFloatSetting;
import com.gameplat.model.entity.setting.SysFloatType;

import java.util.List;

public interface SysFloatTypeService extends IService<SysFloatType> {

  /**
   * 查询游戏浮窗类型列表
   *
   * @param sysFloatTypeVo 游戏浮窗类型
   * @return 游戏浮窗类型集合
   */
  List<SysFloatTypeVo> selectSysFloatTypeList(SysFloatTypeVo sysFloatTypeVo);

  /**
   * 新增游戏浮窗类型
   *
   * @param sysFloatSetting 游戏浮窗类型
   */
  void insertSysFloat(SysFloatSetting sysFloatSetting);

  void updateFloat(SysFloatSetting sysFloatSetting);

  void updateBatch(List<SysFloatSetting> sysFloatSettings);

  void updateFloatType(SysFloatType sysFloatType);

  void updateShowPosition(String showPositions);
}
