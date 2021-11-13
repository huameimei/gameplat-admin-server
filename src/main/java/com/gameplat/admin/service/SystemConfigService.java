package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.SysFileConfigVO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.model.vo.SysSmsConfigVO;

import java.util.List;

public interface SystemConfigService {

  List<SysSmsConfigVO> findSmsList();

  List<SysFileConfigVO> findFileList();

  void updateSmsConfig(SysSmsConfigDTO sysSmsConfigDTO);

  void updateFileConfig(SysFileConfigDTO sysFileConfigDTO);

  void configDataEdit(OperSystemConfigDTO dto);

  IPage<SysSmsAreaVO> findSmsAreaList(PageDTO<SysSmsArea> page, SysSmsAreaQueryDTO dto);

  void smsAreaEdit(OperSysSmsAreaDTO dto);

  void smsAreaDelete(Long id);
}
