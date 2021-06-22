package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.SysQuickReplyConvert;
import com.gameplat.admin.dao.SysQuickReplyMapper;
import com.gameplat.admin.model.dto.SysQuickReplayQueryDto;
import com.gameplat.admin.model.entity.SysQuickReply;
import com.gameplat.admin.model.vo.SysQuickReplyVo;
import com.gameplat.admin.service.SysQuickReplyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author Lenovo */
@Service
public class SysQuickReplyServiceImpl extends ServiceImpl<SysQuickReplyMapper, SysQuickReply>
    implements SysQuickReplyService {

  @Autowired
  private SysQuickReplyConvert sysQuickReplyConvert;

  @Override
  public IPage<SysQuickReplyVo> queryPage(Page<SysQuickReply> page,
      SysQuickReplayQueryDto queryDto) {
    LambdaQueryWrapper<SysQuickReply> query = Wrappers.lambdaQuery();
    if (StringUtils.isNotBlank(queryDto.getMessage())){
      query.like(SysQuickReply::getMessage,queryDto.getMessage());
    }
     return this.page(page, query).convert(sysQuickReplyConvert::toVo);
  }
}
