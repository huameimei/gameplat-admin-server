package com.gameplat.admin.controller.open;

import static com.gameplat.common.constant.ServiceName.ADMIN_SERVICE;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.TpPayChannelAddDTO;
import com.gameplat.admin.model.dto.TpPayChannelEditDTO;
import com.gameplat.admin.model.dto.TpPayChannelQueryDTO;
import com.gameplat.admin.model.entity.TpPayChannel;
import com.gameplat.admin.model.vo.TpPayChannelVO;
import com.gameplat.admin.service.TpPayChannelService;
import com.gameplat.common.constant.ServiceApi;
import com.gameplat.log.annotation.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.API + "/tpPayChannels")
public class TpPayChannelController {

  @Autowired private TpPayChannelService tpPayChannelService;

  @DeleteMapping("/remove")
  @Log(module = ADMIN_SERVICE, desc = "'后台管理删除支付通道'")
  public void remove(@RequestBody Long id) {
    tpPayChannelService.delete(id);
  }

  @PostMapping("/add")
  @Log(module = ADMIN_SERVICE, desc = "'支付通道新增'")
  public void add(@RequestBody TpPayChannelAddDTO dto) {
    tpPayChannelService.save(dto);
  }

  @PostMapping("/edit")
  @Log(module = ADMIN_SERVICE, desc = "'支付通道更新'")
  public void edit(@RequestBody TpPayChannelEditDTO dto) {
    tpPayChannelService.update(dto);
  }

  @PostMapping("/editStatus")
  @Log(module = ADMIN_SERVICE, desc = "'支付通道更新状态'")
  public void updateStatus(Long id, Integer status) {
    tpPayChannelService.updateStatus(id, status);
  }

  @PostMapping("/page")
  public IPage<TpPayChannelVO> queryPage(Page<TpPayChannelVO> page, TpPayChannelQueryDTO dto) {
    return tpPayChannelService.findTpPayChannelPage(page, dto);
  }

  @GetMapping("/get")
  public TpPayChannel get(Long id) {
    return tpPayChannelService.getById(id);
  }
}
