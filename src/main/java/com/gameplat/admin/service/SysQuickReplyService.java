package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysQuickReplayQueryDto;
import com.gameplat.admin.model.entity.SysQuickReply;
import com.gameplat.admin.model.vo.SysQuickReplyVo;

/**
 * 会员层级配置
 * @author Lenovo
 */
public interface SysQuickReplyService extends IService<SysQuickReply> {


  /**
   * 代付接口列表
   *
   * @param page Page
   * @param queryDto PpInterfaceQueryDTO
   * @return IPage
   */
  IPage<SysQuickReplyVo> queryPage(Page<SysQuickReply> page, SysQuickReplayQueryDto queryDto);


}
