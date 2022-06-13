package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.MessageFeedbackAddDTO;
import com.gameplat.admin.model.dto.MessageFeedbackQueryDTO;
import com.gameplat.admin.model.vo.MessageFeedbackVO;
import com.gameplat.admin.service.MessageFeedbackService;
import com.gameplat.model.entity.message.MessageFeedback;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 意见反馈
 *
 * @author lily
 */
@Slf4j
@Tag(name = "意见反馈")
@RestController
@RequestMapping("/api/admin/message/feedback")
public class OpenMessageFeedbackController {

  @Autowired private MessageFeedbackService messageFeedbackService;

  @Operation(summary = "意见反馈列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('notice:feedback:view')")
  public IPage<MessageFeedbackVO> getList(
      @Validated PageDTO<MessageFeedback> page, MessageFeedbackQueryDTO dto) {
    return messageFeedbackService.getList(page, dto);
  }

  @Operation(summary = "根据ID查询意见反馈内容")
  @GetMapping("/getById")
  @PreAuthorize("hasAuthority('notice:feedback:view')")
  public MessageFeedbackVO getById(@RequestParam(name = "id") Long id) {
    return messageFeedbackService.getById(id);
  }

  @Operation(summary = "读反馈")
  @PostMapping("/read")
  //  @PreAuthorize("hasAuthority('notice:feedback:read')")
  public void updateMessage(Long id) {
    messageFeedbackService.updateMessage(id);
  }

  @Operation(summary = "新增意见反馈")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('notice:feedback:add')")
  public void insertMessage(@Validated MessageFeedbackAddDTO dto) {
    messageFeedbackService.insertMessage(dto);
  }

  @Operation(summary = "删除意见反馈")
  @PostMapping("/remove")
  @PreAuthorize("hasAuthority('notice:feedback:remove')")
  public void removeMessage(@RequestParam(name = "id") Long id) {
    messageFeedbackService.removeMessage(id);
  }

  @Operation(summary = "查看已回复信件")
  @GetMapping("/getReplyContent")
  @PreAuthorize("hasAuthority('notice:feedback:view')")
  public IPage<MessageFeedbackVO> getReplyContent(PageDTO<MessageFeedback> page) {
    return messageFeedbackService.getReplyContent(page);
  }
}
