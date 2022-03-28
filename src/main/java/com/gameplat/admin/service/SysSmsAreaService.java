package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SmsAreaAddDTO;
import com.gameplat.admin.model.dto.SmsAreaEditDTO;
import com.gameplat.admin.model.dto.SmsAreaQueryDTO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.model.entity.sys.SysSmsArea;

public interface SysSmsAreaService extends IService<SysSmsArea> {

  IPage<SysSmsAreaVO> findSmsAreaList(PageDTO<SysSmsArea> page, SmsAreaQueryDTO queryDTO);

  void addSmsArea(SmsAreaAddDTO addDTO);

  void editSmsArea(SmsAreaEditDTO editDTO);

  void deleteAreaById(Long id);

  void changeStatus(Long id, Integer status);

  void setDefaultStatus(Long id, Integer status);
}
