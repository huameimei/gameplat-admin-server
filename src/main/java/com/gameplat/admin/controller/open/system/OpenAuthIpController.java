package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.AuthIpDTO;
import com.gameplat.admin.model.dto.OperAuthIpDTO;
import com.gameplat.admin.model.vo.AuthIpVo;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.lang.Assert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysAuthIp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * ip白名单
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/system/authIp")
public class OpenAuthIpController {

  @Autowired private SysAuthIpService authIpService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:authIp:view')")
  public IPage<AuthIpVo> list(PageDTO<SysAuthIp> page, AuthIpDTO authIpDTO) {
    return authIpService.selectAuthIpList(page, authIpDTO);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:authIp:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'添加IP白名单 【'+#authIpDTO.ip+'】'")
  public void save(OperAuthIpDTO authIpDTO) {
    Assert.notEmpty(authIpDTO.getIp(), "缺少ip参数");
    authIpService.insertAuthip(authIpDTO);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:authIp:edit')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改IP白名单 【'+#authIpDTO.ip+'】'")
  public void update(@RequestBody OperAuthIpDTO authIpDTO) {
    Assert.notNull(authIpDTO.getId(), "缺少参数");
    Assert.notEmpty(authIpDTO.getIp(), "缺少ip参数");
    authIpService.updateAuthIp(authIpDTO);
  }

  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('system:authIp:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除IP白名单 id='+#ids")
  public void remove(@RequestBody String ids) {
    Assert.notEmpty(ids, "参数不全");
    authIpService.deleteBatch(ids);
  }

  @GetMapping("/checkAuthIpUnique")
  public boolean checkAuthIpUnique(String ip) {
    Assert.notEmpty(ip, "缺少参数");
    return authIpService.checkAuthIpUnique(ip);
  }
}
