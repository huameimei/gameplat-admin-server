package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.EmailDTO;
import com.gameplat.admin.model.dto.OperEmailDTO;
import com.gameplat.admin.model.vo.EmailVO;
import com.gameplat.model.entity.sys.SysEmail;

/**
 * 邮件记录 业务层
 *
 * @author Lenovo
 */
public interface SysEmailService extends IService<SysEmail> {

  IPage<EmailVO> selectEmailList(IPage<SysEmail> page, EmailDTO emailDTO);

  void insertEmail(OperEmailDTO emailDTO);

  void updateEmail(OperEmailDTO emailDTO);

  void cleanEmail();
}
