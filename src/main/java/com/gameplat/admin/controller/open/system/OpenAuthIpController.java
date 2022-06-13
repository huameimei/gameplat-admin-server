package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.AuthIpDTO;
import com.gameplat.admin.model.dto.OperAuthIpDTO;
import com.gameplat.admin.model.vo.AuthIpVo;
import com.gameplat.admin.service.SysAuthIpService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.group.Groups;
import com.gameplat.common.lang.Assert;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.sys.SysAuthIp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.websocket.server.PathParam;
import java.util.Map;

@Tag(name = "IP白名单")
@Validated
@RestController
@RequestMapping("/api/admin/system/authIp")
public class OpenAuthIpController {

  @Autowired private SysAuthIpService authIpService;

  @Operation(summary = "查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('system:authIp:view')")
  public IPage<AuthIpVo> list(PageDTO<SysAuthIp> page, AuthIpDTO authIpDTO) {
    return authIpService.selectAuthIpList(page, authIpDTO);
  }

  @Operation(summary = "添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('system:authIp:add')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'添加IP白名单 【'+#dto.ip+'】'")
  public void save(@Validated(Groups.INSERT.class) OperAuthIpDTO dto) {
    authIpService.addAuthIp(dto);
  }

  @Operation(summary = "编辑")
  @PostMapping("/edit")
  @PreAuthorize("hasAuthority('system:authIp:edit')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'修改IP白名单 【'+#dto.ip+'】'")
  public void update(@RequestBody @Validated(Groups.UPDATE.class) OperAuthIpDTO dto) {
    authIpService.updateAuthIp(dto);
  }

  @Operation(summary = "删除")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('system:authIp:remove')")
  @Log(module = ServiceName.ADMIN_SERVICE, type = LogType.ADMIN, desc = "'删除IP白名单 id='+#ids")
  public void remove(@RequestBody @NotEmpty(message = "缺少参数") Map<String, String> map) {
    Assert.notNull(map.get("ids"), "id不能为空！");
    authIpService.deleteBatch(map.get("ids"));
  }

  @Operation(summary = "检查唯一")
  @GetMapping("/checkAuthIpUnique/{ip}")
  public boolean checkAuthIpUnique(@PathParam("ip") String ip) {
    return authIpService.checkAuthIpUnique(ip);
  }
}
