package com.gameplat.admin.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.QuickReplyConvert;
import com.gameplat.admin.mapper.QuickReplyMapper;
import com.gameplat.admin.model.domain.QuickReply;
import com.gameplat.admin.model.dto.QuickReplyDTO;
import com.gameplat.admin.model.vo.QuickReplyVO;
import com.gameplat.admin.service.QuickReplyService;
import com.gameplat.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

/**
 * 快捷回复 服务实现层
 *
 * @author three
 */
@Service
@RequiredArgsConstructor
public class QuickReplyServiceImpl extends ServiceImpl<QuickReplyMapper, QuickReply>
    implements QuickReplyService {

  private final QuickReplyConvert quickReplyConvert;

  @Override
  @SentinelResource(value = "selectQuickReplyList")
  public IPage<QuickReplyVO> selectQuickReplyList(IPage<QuickReply> page, QuickReplyDTO replyDTO) {
    LambdaQueryChainWrapper<QuickReply> queryWrapper =
        this.lambdaQuery()
            .eq(
                ObjectUtils.isNotEmpty(replyDTO.getMessageType()),
                QuickReply::getMessageType,
                replyDTO.getMessageType());
    if (Objects.equals(replyDTO.getMessageFuzzy(), 1)) {
      queryWrapper.like(
          ObjectUtils.isNotNull(replyDTO.getMessageFuzzy()),
          QuickReply::getMessage,
          replyDTO.getMessage());
    } else {
      queryWrapper.eq(
          ObjectUtils.isNotEmpty(replyDTO.getMessage()),
          QuickReply::getMessage,
          replyDTO.getMessage());
    }
    return queryWrapper.page(page).convert(quickReplyConvert::toVo);
  }

  @Override
  @SentinelResource(value = "insertQuickReply")
  public void insertQuickReply(QuickReplyDTO replyDTO) {
    QuickReply reply = quickReplyConvert.toEntity(replyDTO);
    if (!this.save(reply)) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  @SentinelResource(value = "updateQuickReply")
  public void updateQuickReply(QuickReplyDTO replyDTO) {
    QuickReply reply = quickReplyConvert.toEntity(replyDTO);
    if (!this.updateById(reply)) {
      throw new ServiceException("更新失败!");
    }
  }

  @Override
  @SentinelResource(value = "deleteQuickReply")
  public void deleteQuickReply(String ids) {
    Long[] arrIds = Convert.toLongArray(ids);
    if (!this.removeByIds(Arrays.asList(arrIds))) {
      throw new ServiceException("批量删除失败!");
    }
  }
}