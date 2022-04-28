package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.vo.SysFloatTypeVo;
import com.gameplat.model.entity.setting.SysFloatSetting;
import com.gameplat.model.entity.setting.SysFloatType;

import java.util.List;

public interface SysFloatTypeMapper extends BaseMapper<SysFloatType> {
  /**
   * 查询游戏浮窗类型列表
   *
   * @param sysFloatTypeVo 游戏浮窗类型
   * @return 游戏浮窗类型集合
   */
  List<SysFloatTypeVo> selectSysFloatTypeList(SysFloatTypeVo sysFloatTypeVo);

  /**
   * 新增游戏浮窗
   *
   * @param sysFloatSetting 游戏浮窗
   * @return 结果
   */
  void insertSysFloatSetting(SysFloatSetting sysFloatSetting);

  /**
   * 修改游戏浮窗
   *
   * @param sysFloatSetting 游戏浮窗
   * @return 结果
   */
  void updateSysFloat(SysFloatSetting sysFloatSetting);

  void updateBatch(List<SysFloatSetting> sysFloatSettings);

  void updateShowPosition(String showPositions);
}
