package com.gameplat.admin.controller.open.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.ChatPushPlanAddOrEditDTO;
import com.gameplat.admin.model.dto.ChatPushPlanQueryDTO;
import com.gameplat.admin.model.vo.ChatPushPlanVO;
import com.gameplat.admin.service.ChatPushPlanService;
import com.gameplat.model.entity.chart.ChatPushPlan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author lily
 * @description 聊天计划推送管理
 * @date 2022/2/16
 */
@Tag(name = "聊天计划推送管理")
@RestController
@RequestMapping("/api/admin/chat/pushPlan")
public class ChatPushPlanController {

  @Autowired private ChatPushPlanService chatPushPlanService;

  @Operation(summary = "分页列表")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('chat:pushPlan:view')")
  public IPage<ChatPushPlanVO> page(PageDTO<ChatPushPlan> page, ChatPushPlanQueryDTO dto) {
    return chatPushPlanService.page(page, dto);
  }

  @Operation(summary = "增")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('chat:pushPlan:add')")
  public void add(@Validated @RequestBody ChatPushPlanAddOrEditDTO dto) {
    chatPushPlanService.add(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('chat:pushPlan:edit')")
  public void edit(@Validated @RequestBody  ChatPushPlanAddOrEditDTO dto) {
    chatPushPlanService.edit(dto);
  }

  @Operation(summary = "删")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('chat:pushPlan:remove')")
  public void remove(@PathVariable Long id) {
    chatPushPlanService.remove(id);
  }
}
