package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.PushMessage;
import com.gameplat.admin.model.dto.PushMessageAddDTO;
import com.gameplat.admin.model.dto.PushMessageDTO;
import com.gameplat.admin.model.dto.PushMessageRemoveDTO;
import com.gameplat.admin.model.vo.PushMessageVO;

public interface PushMessageService extends IService<PushMessage> {


    IPage<PushMessageVO> findPushMessageList(PageDTO<PushMessage> page, PushMessageDTO pushMessageDTO);

    void insertPushMessage(PushMessageAddDTO pushMessageAddDTO);

    void deletePushMessage(Long id);

    void deleteBatchPushMessage(Long[] ids);

    void deleteByCondition(PushMessageRemoveDTO pushMessageRemoveDTO);
}
