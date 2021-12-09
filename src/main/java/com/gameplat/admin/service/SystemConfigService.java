package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.dto.OperSysSmsAreaDTO;
import com.gameplat.admin.model.dto.OperSystemConfigDTO;
import com.gameplat.admin.model.dto.SysFileConfigDTO;
import com.gameplat.admin.model.dto.SysSmsAreaQueryDTO;
import com.gameplat.admin.model.dto.SysSmsConfigDTO;
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

  SysDictData findActivityTypeCodeList(String language);
}
