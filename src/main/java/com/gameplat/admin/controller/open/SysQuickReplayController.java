package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.SysQuickReplayQueryDto;
import com.gameplat.admin.model.entity.SysQuickReply;
import com.gameplat.admin.model.vo.SysQuickReplyVo;
import com.gameplat.admin.service.SysQuickReplyService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( ServiceApi.OPEN_API + "/sysQuickReply")
public class SysQuickReplayController {

  @Autowired
  private SysQuickReplyService sysQuickReplyService;

  @GetMapping("/list")
  public IPage<SysQuickReplyVo> list(Page<SysQuickReply> page,SysQuickReplayQueryDto queryDto){
      return sysQuickReplyService.queryPage(page, queryDto);
  }
}
