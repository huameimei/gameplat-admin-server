package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MessageFeedback;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.MessageFeedbackVO;
import com.gameplat.admin.service.MessageFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 意见反馈
 * @author lily
 */
@Slf4j
@Api(tags = "意见反馈")
@RestController
@RequestMapping("/api/admin/message/feeback")
public class OpenMessageFeedbackController {

  @Autowired private MessageFeedbackService messageFeedbackService;

  @ApiOperation(value = "意见反馈列表")
  @GetMapping("/get")
  public IPage<MessageFeedbackVO> getList(PageDTO<MessageFeedback> page, MessageFeedbackQueryDTO dto) {
    return messageFeedbackService.getList(page, dto);
  }

  @ApiOperation(value = "根据ID查询意见反馈内容")
  @GetMapping("/getById")
  public MessageFeedbackVO getById(Long id) {
    return messageFeedbackService.getById(id);
  }

  @ApiOperation(value = "修改意见反馈")
  @PutMapping("/edit")
  public void updateMessage(@RequestBody MessageFeedbackUpdateDTO dto) {
    messageFeedbackService.updateMessage(dto);
  }

  @ApiOperation(value = "新增意见反馈")
  @PostMapping("/add")
  public void insertMessage(@Validated @RequestBody MessageFeedbackAddDTO dto) {
    messageFeedbackService.insertMessage(dto);
  }

  @ApiOperation(value = "删除意见反馈")
  @DeleteMapping("/remove")
  public void removeMessage(Long id) {
    messageFeedbackService.removeMessage(id);
  }


}
