package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gameplat.admin.model.dto.SysAuthIpAddDto;
import com.gameplat.admin.model.dto.SysAuthIpQueryDto;
import com.gameplat.admin.model.entity.SysAuthIp;
import com.gameplat.admin.model.vo.SysAuthIpVo;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.OPEN_API + "/authIp")
public class SysAuthIpController {

  @Autowired private SysAuthIpService sysAuthIpService;

  /** 查询所有IP白名单 */
  @GetMapping(value = "/queryAll")
  public IPage<SysAuthIpVo> queryAll(IPage<SysAuthIp> sysAuthIp, SysAuthIpQueryDto queryDto) {
    return sysAuthIpService.queryPage(sysAuthIp, queryDto);
  }

  @PostMapping(value = "/save")
  public void save(SysAuthIpAddDto sysAuthIpAddDto) {
    sysAuthIpService.save(sysAuthIpAddDto);
  }

  @DeleteMapping(value = "/delete/{id}")
  public void delete(@PathVariable("id") Long id) {
    sysAuthIpService.delete(id);
  }
}
