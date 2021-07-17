package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.SysQuickReplayQueryDTO;
import com.gameplat.admin.model.entity.SysQuickReply;
import com.gameplat.admin.model.vo.SysQuickReplyVO;
import com.gameplat.admin.service.SysQuickReplyService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.API + "/sysQuickReply")
public class SysQuickReplayController {

  @Autowired private SysQuickReplyService sysQuickReplyService;

  @GetMapping("/list")
  public IPage<SysQuickReplyVO> list(Page<SysQuickReply> page, SysQuickReplayQueryDTO queryDto) {
    return sysQuickReplyService.queryPage(page, queryDto);
  }
}
