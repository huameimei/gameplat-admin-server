package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MessageDistributeQueryDTO;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;
import com.gameplat.admin.model.dto.MessageInfoEditDTO;
import com.gameplat.admin.model.dto.MessageInfoQueryDTO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.admin.model.vo.MessageInfoVO;
import com.gameplat.admin.service.MessageInfoService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.message.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人消息
 *
 * @author lily
 */
@Tag(name = "消息管理")
@RestController
@RequestMapping("/api/admin/message")
public class OpenMessageInfoController {

  @Autowired private MessageInfoService messageInfoService;

  @Operation(summary = "分页查询消息")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('operator:message:view')")
  public IPage<MessageInfoVO> page(
      @Parameter(hidden = true) PageDTO<Message> page, MessageInfoQueryDTO dto) {
    return messageInfoService.findMessageList(page, dto);
  }

  @Operation(summary = "新增消息")
  @PostMapping("/save")
  @PreAuthorize("hasAuthority('operator:message:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'消息管理-->新增消息:' + #dto")
  public void save(@Validated MessageInfoAddDTO dto) {
    messageInfoService.insertMessage(dto);
  }

  @Operation(summary = "编辑消息")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('operator:message:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'消息管理-->编辑消息:' + #dto")
  public void edit(@Validated MessageInfoEditDTO dto) {
    messageInfoService.editMessage(dto);
  }

  @Operation(summary = "删除消息")
  @PostMapping("/remove")
  @PreAuthorize("hasAuthority('operator:message:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'消息管理-->删除消息:' + #ids")
  public void remove(String ids) {
    messageInfoService.deleteBatchMessage(ids);
  }

  @Operation(summary = "查看推送目标会员")
  @GetMapping("/distribute/page")
  @PreAuthorize("hasAuthority('operator:message:distributePage')")
  public IPage<MessageDistributeVO> distributePage(
      @Parameter(hidden = true) Page<Member> page, MessageDistributeQueryDTO dto) {
    return messageInfoService.findMessageDistributeList(page, dto);
  }
}
