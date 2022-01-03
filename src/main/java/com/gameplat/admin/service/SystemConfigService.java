package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.AgentContacaVO;
import com.gameplat.admin.model.vo.SysFileConfigVO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.model.vo.SysSmsConfigVO;
import com.gameplat.common.model.bean.EmailConfig;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface SystemConfigService {

  List<AgentContacaVO> findAgentContacaList();

  void updateAgentContaca(AgentContacaDTO agentContacaDTO);

  List<SysSmsConfigVO> findSmsList();

  List<SysFileConfigVO> findFileList();

  void updateSmsConfig(SysSmsConfigDTO sysSmsConfigDTO);

  void updateFileConfig(SysFileConfigDTO sysFileConfigDTO);

  void configDataEdit(OperSystemConfigDTO dto);

  IPage<SysSmsAreaVO> findSmsAreaList(PageDTO<SysSmsArea> page, SysSmsAreaQueryDTO dto);

  void smsAreaEdit(OperSysSmsAreaDTO dto);

  void smsAreaDelete(Long id);

  EmailConfig findEmailConfig();

  void updateEmail(EmailConfig emailConfig);
}
