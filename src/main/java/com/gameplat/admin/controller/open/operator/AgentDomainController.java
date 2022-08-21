package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.AgentDomainDTO;
import com.gameplat.admin.model.vo.AgentDomainVO;
import com.gameplat.admin.service.AgentDomainService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.spread.AgentDomain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "代理域名")
@RestController
@RequestMapping("/api/admin/operator/diffusion/agent")
public class AgentDomainController {

  @Autowired private AgentDomainService domainService;

  @Operation(summary = "代理域名列表")
  @GetMapping("/agentDomainList")
  @PreAuthorize("hasAuthority('diffusion:agnetDomain:list')")
  public IPage<AgentDomainVO> agentDomainList(PageDTO<AgentDomain> page, AgentDomainDTO domainDTO) {
    return domainService.agentDomainList(page, domainDTO);
  }

  @Operation(summary = "新增")
  @PostMapping("/createAgentDomain")
  @PreAuthorize("hasAuthority('diffusion:agnetDomain:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'代理域名-->新增:' + #domainDTO")
  public void createAgentDomain(@RequestBody AgentDomainDTO domainDTO) {
    domainService.createAgentDomain(domainDTO);
  }

  @Operation(summary = "删除")
  @PostMapping("/deleteAgentDomain")
  @PreAuthorize("hasAuthority('diffusion:agnetDomain:delete')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'代理域名-->删除:' + #id")
  public void deleteAgentDomain(@RequestParam Integer id) {
    domainService.deleteAgentDomain(id);
  }

  @Operation(summary = "修改")
  @PostMapping("/updateAgentDomain")
  @PreAuthorize("hasAuthority('diffusion:agnetDomain:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'代理域名-->修改:' + #domainDTO")
  public void updateAgentDomain(@RequestBody AgentDomainDTO domainDTO) {
    domainService.updateAgentDomain(domainDTO);
  }

  @Operation(summary = "导出")
  @GetMapping("/exportList")
  @PreAuthorize("hasAuthority('diffusion:agnetDomain:export')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'代理域名-->导出:' + #domainDTO")
  public void exportList(AgentDomainDTO domainDTO, HttpServletResponse response) {
    domainService.exportList(domainDTO, response);
  }
}
