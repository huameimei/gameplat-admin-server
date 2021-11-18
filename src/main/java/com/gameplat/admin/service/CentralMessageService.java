package com.gameplat.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.CentralMessage;
import com.gameplat.admin.model.vo.CentralMessageVO;

/**
 * @author Lily
 */
public interface CentralMessageService extends IService<CentralMessage> {

    IPage<CentralMessageVO> selectCentralMessageList(IPage<CentralMessage> page);
}
