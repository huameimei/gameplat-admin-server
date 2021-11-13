package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.QuickReply;
import com.gameplat.admin.model.dto.QuickReplyDTO;
import com.gameplat.admin.model.vo.QuickReplyVO;
import com.gameplat.admin.service.QuickReplyService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 快捷回复控制器
 *
 * @author three
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/system/quick")
public class OpenQuickReplyController {

  @Autowired private QuickReplyService replyService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:quickReply:view')")
  public IPage<QuickReplyVO> list(PageDTO<QuickReply> page, QuickReplyDTO replyDTO) {
    return replyService.selectQuickReplyList(page, replyDTO);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:quickReply:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'添加快捷回复配置 【'+#replyDTO.message+'】'")
  public void save(@RequestBody QuickReplyDTO replyDTO) {
    if (StringUtils.isBlank(replyDTO.getMessage())) {
      throw new ServiceException("快捷回复信息不能为空");
    }
    replyService.insertQuickReply(replyDTO);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:quickReply:edit')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改快捷回复配置 【'+#replyDTO.message+'】'")
  public void update(@RequestBody QuickReplyDTO replyDTO) {
    if (StringUtils.isNull(replyDTO.getId())) {
      throw new ServiceException("缺少主键");
    }
    if (StringUtils.isBlank(replyDTO.getMessage())) {
      throw new ServiceException("快捷回复信息不能为空");
    }
    replyService.updateQuickReply(replyDTO);
  }

  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('system:quickReply:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除快捷回复配置 id='+#ids")
  public void remove(@RequestBody String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("缺少主键");
    }
    replyService.deleteQuickReply(ids);
  }
}
