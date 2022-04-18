package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.MessageFeedbackConvert;
import com.gameplat.admin.mapper.MessageFeedbackMapper;
import com.gameplat.admin.model.dto.MessageFeedbackAddDTO;
import com.gameplat.admin.model.dto.MessageFeedbackQueryDTO;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;
import com.gameplat.admin.model.vo.MessageFeedbackVO;
import com.gameplat.admin.service.MessageFeedbackService;
import com.gameplat.admin.service.MessageInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.message.MessageFeedback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lily
 * @description 意见反馈
 * @date 2022/1/12
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class MessageFeedbackServiceImpl extends ServiceImpl<MessageFeedbackMapper, MessageFeedback>
    implements MessageFeedbackService {

  @Autowired private MessageFeedbackConvert messageFeedbackConvert;

  @Autowired private MessageInfoService messageInfoService;

  @Override
  public void insertMessage(MessageFeedbackAddDTO dto) {
    //回复信件
    dto.setType(2);
    MessageFeedback messageFeedback = messageFeedbackConvert.toEntity(dto);
    this.save(messageFeedback);

    MessageInfoAddDTO addDTO = new MessageInfoAddDTO();
    if (StringUtils.isNotBlank(dto.getTitle())) {
      addDTO.setTitle(dto.getTitle());
    }

    addDTO.setContent(dto.getContent());
    addDTO.setCategory(4);
    addDTO.setPushRange(2);
    addDTO.setLinkAccount(dto.getUsername());
    addDTO.setStatus(1);
    addDTO.setFeedbackType(dto.getLetterType());
    addDTO.setType(dto.getType());
    if (StringUtils.isNotBlank(dto.getImgUrl())) {
      addDTO.setFeedbackImage(dto.getImgUrl());
    }
    messageInfoService.insertMessage(addDTO);
  }

  @Override
  public void updateMessage(Long id) {
    Assert.isTrue(
        this.lambdaUpdate()
            .set(MessageFeedback::getIsRead, 1)
            .eq(MessageFeedback::getId, id)
            .update(new MessageFeedback()),
        "已读失败!");
  }

  @Override
  public void removeMessage(Long id) {
    this.removeById(id);
  }

  @Override
  public IPage<MessageFeedbackVO> getList(
      PageDTO<MessageFeedback> page, MessageFeedbackQueryDTO dto) {
    return this.lambdaQuery()
        .like(ObjectUtil.isNotEmpty(dto.getTitle()), MessageFeedback::getTitle, dto.getTitle())
        .eq(ObjectUtil.isNotEmpty(dto.getIsRead()), MessageFeedback::getIsRead, dto.getIsRead())
        .eq(ObjectUtil.isNotEmpty(dto.getType()), MessageFeedback::getType, dto.getType())
        .ge(
            ObjectUtil.isNotEmpty(dto.getBeginTime()),
            MessageFeedback::getCreateTime,
            dto.getBeginTime())
        .le(
            ObjectUtil.isNotEmpty(dto.getEndTime()),
            MessageFeedback::getCreateTime,
            dto.getEndTime())
        .ne(MessageFeedback::getType, 4)
        .orderByDesc(MessageFeedback::getCreateTime)
        .page(page)
        .convert(messageFeedbackConvert::toVo);
  }

  @Override
  public MessageFeedbackVO getById(Long id) {
    if (ObjectUtil.isEmpty(id)) {
      throw new ServiceException("反馈id不能为空");
    }
    MessageFeedback messageFeedback = this.lambdaQuery().eq(MessageFeedback::getId, id).one();
    return messageFeedbackConvert.toVo(messageFeedback);
  }

  @Override
  public IPage<MessageFeedbackVO> getReplyContent(PageDTO<MessageFeedback> page) {
    return this.lambdaQuery()
        .in(MessageFeedback::getType, 2, 4)
        .eq(MessageFeedback::getStatus, 1)
        .orderByDesc(MessageFeedback::getCreateTime)
        .page(page)
        .convert(messageFeedbackConvert::toVo);
  }
}
