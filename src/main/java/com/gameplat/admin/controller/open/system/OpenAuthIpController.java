package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SysAuthIp;
import com.gameplat.admin.model.dto.AuthIpDTO;
import com.gameplat.admin.model.dto.OperAuthIpDTO;
import com.gameplat.admin.model.vo.AuthIpVo;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
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
    if (StringUtils.isBlank(authIpDTO.getIp())) {
      throw new ServiceException("缺少ip参数");
    }
    authIpService.insertAuthip(authIpDTO);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('system:authIp:edit')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.ADMIN,
      desc = "'修改IP白名单 【'+#authIpDTO.ip+'】'")
  public void update(@RequestBody OperAuthIpDTO authIpDTO) {
    if (StringUtils.isNull(authIpDTO.getId())) {
      throw new ServiceException("缺少参数");
    }

    if (StringUtils.isBlank(authIpDTO.getIp())) {
      throw new ServiceException("缺少ip参数");
    }
    authIpService.updateAuthIp(authIpDTO);
  }

  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('system:authIp:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除IP白名单 id='+#ids")
  public void remove(@RequestBody String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("参数不全");
    }
    authIpService.deleteBatch(ids);
  }

  @GetMapping("/checkAuthIpUnique")
  public boolean checkAuthIpUnique(String ip) {
    if (StringUtils.isBlank(ip)) {
      throw new ServiceException("参数不全");
    }
    return authIpService.checkAuthIpUnique(ip);
  }
}
