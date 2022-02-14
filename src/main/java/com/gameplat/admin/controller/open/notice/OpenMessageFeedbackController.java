package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MessageFeedback;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MessageFeedbackVO;
import com.gameplat.admin.service.MessageFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "意见反馈")
@RestController
@RequestMapping("/api/admin/message/feedback")
public class OpenMessageFeedbackController {

  @Autowired private MessageFeedbackService messageFeedbackService;

  @ApiOperation(value = "意见反馈列表")
  @GetMapping("/list")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
    @ApiImplicitParam(name = "size", value = "每页条数"),
  })
  @PreAuthorize("hasAuthority('notice:feedback:list')")
  public IPage<MessageFeedbackVO> getList(
      @Validated PageDTO<MessageFeedback> page, MessageFeedbackQueryDTO dto) {
    return messageFeedbackService.getList(page, dto);
  }

  @ApiOperation(value = "根据ID查询意见反馈内容")
  @GetMapping("/getById")
  @PreAuthorize("hasAuthority('notice:feedback:view')")
  public MessageFeedbackVO getById(@RequestParam(name = "id") Long id) {
    return messageFeedbackService.getById(id);
  }

  @ApiOperation(value = "读反馈")
  @PutMapping("/read")
  @PreAuthorize("hasAuthority('notice:feedback:read')")
  public void updateMessage(Long id) {
    messageFeedbackService.updateMessage(new MessageFeedbackUpdateDTO(){{
                                                              setIsRead(1);
                                                              setId(id);
    }});
  }

  @ApiOperation(value = "新增意见反馈")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('notice:feedback:edit')")
  public void insertMessage(@Validated MessageFeedbackAddDTO dto) {
    messageFeedbackService.insertMessage(dto);
  }

  @ApiOperation(value = "删除意见反馈")
  @DeleteMapping("/remove")
  @PreAuthorize("hasAuthority('notice:feedback:delete')")
  public void removeMessage(@RequestParam(name = "id") Long id) {
    messageFeedbackService.removeMessage(id);
  }

  @ApiOperation(value = "查看已回复信件")
  @GetMapping("/getReplyContent")
  @PreAuthorize("hasAuthority('notice:feedback:getReplyContent')")
  public IPage<MessageFeedbackVO> getReplyContent(PageDTO<MessageFeedback> page) {
      return messageFeedbackService.getReplyContent(page);
  }

}
