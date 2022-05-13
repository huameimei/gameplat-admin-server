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
import com.gameplat.model.entity.member.Member;
import com.gameplat.model.entity.message.Message;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 个人消息
 *
 * @author lily
 */
@Api(tags = "消息管理")
@RestController
@RequestMapping("/api/admin/message")
public class OpenMessageInfoController {

  @Autowired private MessageInfoService messageInfoService;

  @ApiOperation(value = "分页查询消息")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('operator:message:view')")
  public IPage<MessageInfoVO> page(@ApiIgnore PageDTO<Message> page, MessageInfoQueryDTO dto) {
    return messageInfoService.findMessageList(page, dto);
  }

  @ApiOperation(value = "新增消息")
  @PostMapping("/save")
  @PreAuthorize("hasAuthority('operator:message:add')")
  public void save(@Validated MessageInfoAddDTO dto) {
    messageInfoService.insertMessage(dto);
  }

  @ApiOperation(value = "编辑消息")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('operator:message:edit')")
  public void edit(@Validated MessageInfoEditDTO dto) {
    messageInfoService.editMessage(dto);
  }

  @ApiOperation(value = "删除消息")
  @PostMapping("/remove")
  @PreAuthorize("hasAuthority('operator:message:remove')")
  public void remove(String ids) {
    messageInfoService.deleteBatchMessage(ids);
  }

  @ApiOperation(value = "查看推送目标会员")
  @GetMapping("/distribute/page")
  @PreAuthorize("hasAuthority('operator:message:distributePage')")
  public IPage<MessageDistributeVO> distributePage(
      Page<Member> page, MessageDistributeQueryDTO dto) {
    return messageInfoService.findMessageDistributeList(page, dto);
  }
}
