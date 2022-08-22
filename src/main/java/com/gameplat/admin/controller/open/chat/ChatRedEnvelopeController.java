package com.gameplat.admin.controller.open.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.ChatRedEnvelopeAddDTO;
import com.gameplat.admin.model.dto.ChatRedEnvelopeEditDTO;
import com.gameplat.admin.model.dto.ChatRedEnvelopeQueryDTO;
import com.gameplat.admin.model.dto.ChatRedEnvelopeRecordQueryDTO;
import com.gameplat.admin.model.vo.ChatRedEnvelopeDrawVO;
import com.gameplat.admin.model.vo.ChatRedEnvelopeVO;
import com.gameplat.admin.service.ChatRedEnvelopeDrawService;
import com.gameplat.admin.service.ChatRedEnvelopeRecordService;
import com.gameplat.admin.service.ChatRedEnvelopeService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.chart.ChatRedEnvelope;
import com.gameplat.model.entity.chart.ChatRedEnvelopeDraw;
import com.gameplat.model.entity.chart.ChatRedEnvelopeRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author lily
 * @description 红包管理
 * @date 2022/2/15
 */
@Tag(name = "聊天室红包管理")
@RestController
@RequestMapping("/api/admin/chat/redEnvelope")
public class ChatRedEnvelopeController {

  @Autowired private ChatRedEnvelopeService redEnvelopeService;

  @Autowired private ChatRedEnvelopeDrawService chatRedEnvelopeDrawService;

  @Autowired private ChatRedEnvelopeRecordService chatRedEnvelopeRecordService;

  @Operation(summary = "分页列表")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('chat:redEnvelope:view')")
  public IPage<ChatRedEnvelopeVO> page(PageDTO<ChatRedEnvelope> page, ChatRedEnvelopeQueryDTO dto) {
    return redEnvelopeService.page(page, dto);
  }

  @Operation(summary = "增")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('chat:redEnvelope:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天室红包管理->增:' + #dto")
  public void add(@Validated ChatRedEnvelopeAddDTO dto) {
    redEnvelopeService.add(dto);
  }

  @Operation(summary = "删")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('chat:redEnvelope:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天室红包管理->删:' + #id")
  public void remove(@PathVariable Integer id) {
    redEnvelopeService.remove(id);
  }

  @Operation(summary = "启用禁用")
  @PostMapping("/enable")
  @PreAuthorize("hasAuthority('chat:redEnvelope:enable')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'聊天室红包管理->启用禁用:' + #dto")
  public void enable(@Validated ChatRedEnvelopeEditDTO dto) {
    redEnvelopeService.update(dto);
  }

  @Operation(summary = "红包记录详情")
  @GetMapping("/getRedEnvelopeRecord")
  @PreAuthorize("hasAuthority('chat:redEnvelopeRecord:view')")
  public IPage<ChatRedEnvelopeRecord> page(
      PageDTO<ChatRedEnvelopeRecord> page, @Validated ChatRedEnvelopeRecordQueryDTO dto) {
    return chatRedEnvelopeRecordService.page(page, dto);
  }

  @Operation(summary = "红包领取记录详情")
  @GetMapping("/getRedEnvelopeDraw")
  @PreAuthorize("hasAuthority('chat:redEnvelopeDraw:view')")
  public IPage<ChatRedEnvelopeDrawVO> page(
      PageDTO<ChatRedEnvelopeDraw> page, String id, Integer sort) {
    return chatRedEnvelopeDrawService.page(page, id, sort);
  }
}
