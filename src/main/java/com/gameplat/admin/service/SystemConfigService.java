package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.AgentContacaVO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.common.compent.oss.config.FileConfig;
import com.gameplat.common.compent.sms.SmsConfig;
import com.gameplat.common.model.bean.EmailConfig;

import java.util.List;

public interface SystemConfigService {

  List<AgentContacaVO> findAgentContacaList();

  void updateAgentContaca(AgentContacaDTO agentContacaDTO);

  void delAgentContaca(Long id);

  List<SmsConfig> findSmsList();

  List<FileConfig> findFileList();

  void updateSmsConfig(SmsConfig config);

  void updateFileConfig(FileConfig config);

  void configDataEdit(OperSystemConfigDTO dto);

  EmailConfig findEmailConfig();

  void updateEmail(EmailConfig emailConfig);

  void testSendEmail(EmailTestDTO dto);
}
