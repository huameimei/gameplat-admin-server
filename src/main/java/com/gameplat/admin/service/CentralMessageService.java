package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.CentralMessageVO;
import com.gameplat.model.entity.CentralMessage;

/**
 * @author Lily
 */
public interface CentralMessageService extends IService<CentralMessage> {

  IPage<CentralMessageVO> selectCentralMessageList(IPage<CentralMessage> page);
}
