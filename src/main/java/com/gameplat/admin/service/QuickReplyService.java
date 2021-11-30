package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.QuickReply;
import com.gameplat.admin.model.dto.QuickReplyDTO;
import com.gameplat.admin.model.vo.QuickReplyVO;
import java.util.List;

public interface QuickReplyService extends IService<QuickReply> {

  IPage<QuickReplyVO> selectQuickReplyList(IPage<QuickReply> page, QuickReplyDTO replyDTO);

  void insertQuickReply(QuickReplyDTO replyDTO);

  void updateQuickReply(QuickReplyDTO replyDTO);

  void deleteQuickReply(Long id);

  List<QuickReply> getByType(String messageType);

}
