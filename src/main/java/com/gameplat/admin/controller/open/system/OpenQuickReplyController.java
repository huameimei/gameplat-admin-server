package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.QuickReply;
import com.gameplat.admin.model.dto.QuickReplyDTO;
import com.gameplat.admin.model.vo.QuickReplyVO;
import com.gameplat.admin.service.QuickReplyService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.group.Groups;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public IPage<QuickReplyVO> list(PageDTO<QuickReply> page, QuickReplyDTO dto) {
    return replyService.selectQuickReplyList(page, dto);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:quickReply:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'添加快捷回复配置 【'+#dto.message+'】'")
  public void save(@Validated(Groups.INSERT.class) @RequestBody QuickReplyDTO dto) {
    replyService.insertQuickReply(dto);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:quickReply:edit')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改快捷回复配置 【'+#dto.message+'】'")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody QuickReplyDTO dto) {
    replyService.updateQuickReply(dto);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('system:quickReply:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除快捷回复配置 id='+#ids")
  public void remove(@PathVariable Long id) {
    replyService.deleteQuickReply(id);
  }

  @GetMapping("/getByType/{messageType}")
  @PreAuthorize("hasAuthority('system:quickReply:getByType')")
  public List<QuickReply> getByType(@PathVariable String messageType) {
    return replyService.getByType(messageType);
  }

}
