package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysPayBankAddDTO;
import com.gameplat.admin.model.dto.SysPayBankEditDTO;
import com.gameplat.admin.model.entity.SysPayBank;
import java.util.List;

public interface SysPayBankService extends IService<SysPayBank> {

  /**
   * 修改配置信息
   *
   * @param dto SysPayBankUpdateDTO
   */
  void update(SysPayBankEditDTO dto);

  void updateStatus(Long id, Integer status);

  /**
   * 保存
   *
   * @param dto SysPayBankAddDTO
   */
  void save(SysPayBankAddDTO dto);

  /**
   * 删除配置信息
   *
   * @param id Long
   */
  void delete(Long id);

  /**
   * 配置信息列表
   *
   * @return List
   */
  List<SysPayBank> queryList();
}
