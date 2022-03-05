package com.gameplat.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ChatNoticeConvert;
import com.gameplat.admin.mapper.ChatNoticeMapper;
import com.gameplat.admin.model.dto.ChatNoticeAddDTO;
import com.gameplat.admin.model.dto.ChatNoticeEditDTO;
import com.gameplat.admin.model.dto.ChatNoticeQueryDTO;
import com.gameplat.admin.model.vo.ChatNoticeVO;
import com.gameplat.admin.service.ChatNoticeService;
import com.gameplat.common.lang.Assert;
import com.gameplat.model.entity.chart.ChatNotice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lily
 * @description
 * @date 2022/2/9
 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class ChatNoticeServiceImpl extends ServiceImpl<ChatNoticeMapper, ChatNotice>
    implements ChatNoticeService {

  @Autowired private ChatNoticeConvert chatNoticeConvert;

  /** 分页查询 */
  @Override
  public IPage<ChatNoticeVO> page(PageDTO<ChatNotice> page, ChatNoticeQueryDTO dto) {
    return this.lambdaQuery()
        .eq(ObjectUtil.isNotEmpty(dto.getStatus()), ChatNotice::getStatus, dto.getStatus())
        .like(
            ObjectUtil.isNotEmpty(dto.getNoticeTitle()),
            ChatNotice::getNoticeTitle,
            dto.getNoticeTitle())
        .orderByDesc(ChatNotice::getCreateTime)
        .page(page)
        .convert(chatNoticeConvert::toVo);
  }

  /** 增 */
  @Override
  public void add(ChatNoticeAddDTO dto) {
    this.save(chatNoticeConvert.toEntity(dto));
  }

  /** 删 */
  @Override
  public void remove(Long id) {
    Assert.notNull(id, "id不存在");
    this.removeById(id);
  }

  /** 改 */
  @Override
  public void edit(ChatNoticeEditDTO dto) {
    Assert.notNull(dto.getId(), "id不存在");
    this.updateById(chatNoticeConvert.toEntity(dto));
  }
}
