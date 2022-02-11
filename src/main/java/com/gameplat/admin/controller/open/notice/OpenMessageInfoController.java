package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.Member;
import com.gameplat.admin.model.domain.MessageDistribute;
import com.gameplat.admin.model.domain.MessageInfo;
import com.gameplat.admin.model.dto.MessageDistributeQueryDTO;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;
import com.gameplat.admin.model.dto.MessageInfoEditDTO;
import com.gameplat.admin.model.dto.MessageInfoQueryDTO;
import com.gameplat.admin.model.vo.MemberVO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.admin.model.vo.MessageInfoVO;
import com.gameplat.admin.service.MessageInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 个人消息
 *
 * @author kenvin
 */
@Api(tags = "消息管理")
@RestController
@RequestMapping("/api/admin/message")
public class OpenMessageInfoController {

  @Autowired private MessageInfoService messageInfoService;

  /**
   * 分页查询个人消息
   *
   * @param page
   * @param messageInfoQueryDTO
   * @return
   */
  @ApiOperation(value = "分页查询消息")
  @GetMapping("/page")
  @PreAuthorize("hasAuthority('operator:message:page')")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
    @ApiImplicitParam(name = "size", value = "每页条数"),
  })
  public IPage<MessageInfoVO> page(
      @ApiIgnore PageDTO<MessageInfo> page, MessageInfoQueryDTO messageInfoQueryDTO) {
    return messageInfoService.findMessageList(page, messageInfoQueryDTO);
  }

  /**
   * 新增消息
   *
   * @param messageInfoAddDTO
   */
  @ApiOperation(value = "新增消息")
  @PostMapping("/save")
  @PreAuthorize("hasAuthority('operator:message:save')")
  public void save(@Validated MessageInfoAddDTO messageInfoAddDTO) {
    messageInfoService.insertMessage(messageInfoAddDTO);
  }

  /**
   * 编辑个人消息
   *
   * @param messageInfoEditDTO
   */
  @ApiOperation(value = "编辑消息")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('operator:message:edit')")
  public void edit(@Validated MessageInfoEditDTO messageInfoEditDTO) {
    messageInfoService.editMessage(messageInfoEditDTO);
  }

  /**
   * 删除个人消息
   *
   * @param ids
   */
  @ApiOperation(value = "删除消息")
  @DeleteMapping("/remove")
  @PreAuthorize("hasAuthority('operator:message:remove')")
  public void remove(String ids) {
    messageInfoService.deleteBatchMessage(ids);
  }

  /**
   * 个人消息分发会员列表
   * @return
   */
  @ApiOperation(value = "查看推送目标会员")
  @GetMapping("/distribute/page")
  @PreAuthorize("hasAuthority('operator:message:distributePage')")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
    @ApiImplicitParam(name = "size", value = "每页条数"),
  })
  public IPage<MessageDistributeVO> distributePage(
          Page<Member> page, MessageDistributeQueryDTO messageDistributeQueryDTO) {
    return messageInfoService.findMessageDistributeList(page, messageDistributeQueryDTO);
  }
}
