package com.gameplat.admin.controller.open.notice;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.MessageDistribute;
import com.gameplat.admin.model.domain.MessageInfo;
import com.gameplat.admin.model.dto.MessageDistributeQueryDTO;
import com.gameplat.admin.model.dto.MessageInfoAddDTO;
import com.gameplat.admin.model.dto.MessageInfoEditDTO;
import com.gameplat.admin.model.dto.MessageInfoQueryDTO;
import com.gameplat.admin.model.vo.MessageDictDataVO;
import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.admin.model.vo.MessageInfoVO;
import com.gameplat.admin.service.MessageInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 个人消息
 *
 * @author kenvin
 */
@Api(tags = "个人消息")
@RestController
@RequestMapping("/api/admin/message")
public class OpenMessageInfoController {

  @Autowired private MessageInfoService messageInfoService;

  @ApiOperation(value = "公告消息和个人弹窗消息字典数据列表")
  @GetMapping("/getTypes")
  public MessageDictDataVO getDictData() {
    return messageInfoService.getDictData();
  }

  /**
   * 分页查询个人消息
   *
   * @param page
   * @param messageInfoQueryDTO
   * @return
   */
  @ApiOperation(value = "分页查询个人消息")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('notice:message:list')")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
    @ApiImplicitParam(name = "size", value = "每页条数"),
  })
  public IPage<MessageInfoVO> findMessageList(
      @ApiIgnore PageDTO<MessageInfo> page, MessageInfoQueryDTO messageInfoQueryDTO) {
    return messageInfoService.findMessageList(page, messageInfoQueryDTO);
  }

  /**
   * 新增个人消息
   *
   * @param messageInfoAddDTO
   */
  @ApiOperation(value = "新增个人消息")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('notice:message:add')")
  public void add(@RequestBody MessageInfoAddDTO messageInfoAddDTO) {
    messageInfoService.insertMessage(messageInfoAddDTO);
  }

  /**
   * 编辑个人消息
   *
   * @param messageInfoEditDTO
   */
  @ApiOperation(value = "编辑个人消息")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('notice:message:edit')")
  public void edit(@RequestBody MessageInfoEditDTO messageInfoEditDTO) {
    messageInfoService.editMessage(messageInfoEditDTO);
  }

  /**
   * 删除个人消息
   *
   * @param ids
   */
  @ApiOperation(value = "删除个人消息")
  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('notice:message:remove')")
  public void remove(@RequestBody String ids) {
    messageInfoService.deleteBatchMessage(ids);
  }

  /**
   * 个人消息分发会员列表
   *
   * @param page
   * @param messageDistributeQueryDTO
   * @return
   */
  @ApiOperation(value = "个人消息分发会员列表")
  @GetMapping("/distribute/list")
  @PreAuthorize("hasAuthority('notice:message:list')")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
    @ApiImplicitParam(name = "size", value = "每页条数"),
  })
  public IPage<MessageDistributeVO> findMessageDistributeList(
      @ApiIgnore PageDTO<MessageDistribute> page,
      MessageDistributeQueryDTO messageDistributeQueryDTO) {
    return messageInfoService.findMessageDistributeList(page, messageDistributeQueryDTO);
  }
}
