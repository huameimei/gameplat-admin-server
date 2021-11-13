package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.SpreadConfig;
import com.gameplat.admin.model.dto.SpreadConfigAddDTO;
import com.gameplat.admin.model.dto.SpreadConfigDTO;
import com.gameplat.admin.model.dto.SpreadConfigEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;

public interface SpreadConfigService extends IService<SpreadConfig> {

  IPage<SpreadConfigVO> selectSpreadConfigList(IPage<SpreadConfig> page, SpreadConfigDTO configDTO);

  void insertSpreadConfig(SpreadConfigAddDTO configAddDTO);

  void updateSpreadConfig(SpreadConfigEditDTO configEditDTO);

  void deleteSpreadConfig(String id);

  void changeStatus(SpreadConfigEditDTO configEditDTO);

  /**
   * 增加推广码时间
   *
   * @param id
   */
  void changeReleaseTime(Long id);

  void batchEnableStatus(String ids);
  /**
   * 批量关闭状态
   *
   * @param ids
   */
  void batchDisableStatus(String ids);

  /**
   * 批量删除
   *
   * @param ids
   */
  void batchDeleteSpreadConfig(String ids);
}
