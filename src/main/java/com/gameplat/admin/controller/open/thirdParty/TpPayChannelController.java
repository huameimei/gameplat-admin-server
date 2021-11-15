package com.gameplat.admin.controller.open.thirdParty;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.domain.TpPayChannel;
import com.gameplat.admin.model.dto.TpPayChannelAddDTO;
import com.gameplat.admin.model.dto.TpPayChannelEditDTO;
import com.gameplat.admin.model.dto.TpPayChannelQueryDTO;
import com.gameplat.admin.model.vo.TpPayChannelVO;
import com.gameplat.admin.service.TpPayChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/thirdParty/tpPayChannels")
public class TpPayChannelController {

  @Autowired
  private TpPayChannelService tpPayChannelService;

  @DeleteMapping("/remove/{id}")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:remove')")
  public void remove(@PathVariable Long id) {
    tpPayChannelService.delete(id);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:add')")
  public void add(@RequestBody TpPayChannelAddDTO dto) {
    tpPayChannelService.save(dto);
  }

  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:edit')")
  public void edit(@RequestBody TpPayChannelEditDTO dto) {
    tpPayChannelService.update(dto);
  }

  @PostMapping("/editStatus")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:editStatus')")
  public void updateStatus(Long id, Integer status) {
    tpPayChannelService.updateStatus(id, status);
  }

  @PostMapping("/page")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:page')")
  public IPage<TpPayChannelVO> queryPage(Page<TpPayChannel> page, TpPayChannelQueryDTO dto) {
    return tpPayChannelService.findTpPayChannelPage(page, dto);
  }

  @GetMapping("/get")
  @PreAuthorize("hasAuthority('thirdParty:tpPayChannels:get')")
  public TpPayChannel get(Long id) {
    return tpPayChannelService.getById(id);
  }
}
