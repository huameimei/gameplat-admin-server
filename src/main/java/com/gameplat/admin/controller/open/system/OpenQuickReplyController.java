package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.QuickReplyDTO;
import com.gameplat.admin.model.vo.QuickReplyVO;
import com.gameplat.admin.service.QuickReplyService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.group.Groups;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.QuickReply;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "快捷回复")
@RestController
@RequestMapping("/api/admin/system/quick")
public class OpenQuickReplyController {

  @Autowired private QuickReplyService replyService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:quickReply:view')")
  public IPage<QuickReplyVO> list(PageDTO<QuickReply> page, QuickReplyDTO dto) {
    return replyService.selectQuickReplyList(page, dto);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:quickReply:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'添加快捷回复配置 【'+#dto.message+'】'")
  public void save(@Validated(Groups.INSERT.class) @RequestBody QuickReplyDTO dto) {
    replyService.insertQuickReply(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('system:quickReply:edit')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改快捷回复配置 【'+#dto.message+'】'")
  public void update(@Validated(Groups.UPDATE.class) @RequestBody QuickReplyDTO dto) {
    replyService.updateQuickReply(dto);
  }

  @Operation(summary = "删除")
  @PostMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('system:quickReply:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除快捷回复配置 id='+#ids")
  public void remove(@PathVariable Long id) {
    replyService.deleteQuickReply(id);
  }

  @Operation(summary = "根据类型获取")
  @GetMapping("/getByType/{messageType}")
  @PreAuthorize("hasAuthority('system:quickReply:getByType')")
  public List<QuickReply> getByType(@PathVariable String messageType) {
    return replyService.getByType(messageType);
  }

  @Operation(summary = "获取全部")
  @GetMapping("/getAll")
  //  @PreAuthorize("hasAuthority('system:quickReply:getAll')")
  public List<QuickReply> getAll() {
    return replyService.list();
  }
}
