package com.gameplat.admin.controller.open.payment;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.TpPayChannelAddDTO;
import com.gameplat.admin.model.dto.TpPayChannelEditDTO;
import com.gameplat.admin.model.dto.TpPayChannelQueryDTO;
import com.gameplat.admin.model.vo.TpPayChannelVO;
import com.gameplat.admin.service.TpPayChannelService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.pay.TpPayChannel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "第三方支付通道")
@RestController
@RequestMapping("/api/admin/thirdParty/tpPayChannels")
public class TpPayChannelController {

  @Autowired private TpPayChannelService tpPayChannelService;

  @ApiOperation("删除")
  @PostMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'删除支付通道id=' + #id")
  public void remove(@PathVariable Long id) {
    tpPayChannelService.delete(id);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.RECHARGE,
      desc = "'新增支付通道name=' + #dto.name")
  public void add(@RequestBody TpPayChannelAddDTO dto) {
    tpPayChannelService.save(dto);
  }

  @ApiOperation("编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改支付通道id=' + #dto.id")
  public void edit(@RequestBody TpPayChannelEditDTO dto) {
    tpPayChannelService.update(dto);
  }

  @ApiOperation("修改状态")
  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:editStatus')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.RECHARGE, desc = "'修改支付通道状态id=' + #id")
  public void updateStatus(Long id, Integer status) {
    tpPayChannelService.updateStatus(id, status);
  }

  @ApiOperation("查询")
  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:view')")
  public IPage<TpPayChannelVO> queryPage(Page<TpPayChannel> page, TpPayChannelQueryDTO dto) {
    return tpPayChannelService.findTpPayChannelPage(page, dto);
  }

  @ApiOperation("根据ID获取")
  @GetMapping("/get")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:get')")
  public TpPayChannel get(Long id) {
    return tpPayChannelService.getById(id);
  }
}
