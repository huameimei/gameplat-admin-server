package com.gameplat.admin.controller.open.chat;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ChatPushPlan;
import com.gameplat.admin.model.dto.ChatPushPlanAddOrEditDTO;
import com.gameplat.admin.model.dto.ChatPushPlanQueryDTO;
import com.gameplat.admin.model.vo.ChatPushPlanVO;
import com.gameplat.admin.service.ChatPushPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author lily
 * @description 聊天计划推送管理
 * @date 2022/2/16
 */

@Api(tags = "聊天计划推送管理")
@RestController
@RequestMapping("/api/admin/chat/pushPlan")
public class ChatPushPlanController {

    @Autowired
    private ChatPushPlanService chatPushPlanService;

    /** 分页列表*/
    @ApiOperation(value = "分页列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('chat:pushPlan:page')")
    public IPage<ChatPushPlanVO> page(PageDTO<ChatPushPlan> page, ChatPushPlanQueryDTO dto) {
        return chatPushPlanService.page(page, dto);
    }

    @ApiOperation(value = "增")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('chat:pushPlan:add')")
    public void add(@Validated ChatPushPlanAddOrEditDTO dto) {
        chatPushPlanService.add(dto);
    }

    @ApiOperation(value = "编辑")
    @PutMapping("/edit")
    @PreAuthorize("hasAuthority('chat:pushPlan:edit')")
    public void edit(@Validated ChatPushPlanAddOrEditDTO dto) {
        chatPushPlanService.edit(dto);
    }

    @ApiOperation(value = "删")
    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasAuthority('chat:pushPlan:remove')")
    public void remove(@PathVariable Long id){
        chatPushPlanService.remove(id);
    }

}
