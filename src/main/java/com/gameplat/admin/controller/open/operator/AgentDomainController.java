package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.AgentDomainDTO;
import com.gameplat.admin.model.vo.AgentDomainVO;
import com.gameplat.admin.service.AgentDomainService;
import com.gameplat.model.entity.spread.AgentDomain;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Api(tags = "代理域名")
@Slf4j
@RestController
@RequestMapping("/api/admin/operator/diffusion/agent")
public class AgentDomainController {

  @Autowired private AgentDomainService domainService;

  @ApiOperation("代理域名列表")
  @GetMapping("/agentDomainList")
  @PreAuthorize("hasAuthority('diffusion:agnetDomain:list')")
  public IPage<AgentDomainVO> agentDomainList(PageDTO<AgentDomain> page, AgentDomainDTO domainDTO) {
    return domainService.agentDomainList(page, domainDTO);
  }

  @ApiOperation("新增")
  @PostMapping("/createAgentDomain")
  @PreAuthorize("hasAuthority('diffusion:agnetDomain:add')")
  public void createAgentDomain(@RequestBody AgentDomainDTO domainDTO) {
    domainService.createAgentDomain(domainDTO);
  }

  @ApiOperation("删除")
  @PostMapping("/deleteAgentDomain")
  @PreAuthorize("hasAuthority('diffusion:agnetDomain:delete')")
  public void deleteAgentDomain(@RequestParam Integer id) {
    domainService.deleteAgentDomain(id);
  }

  @ApiOperation("修改")
  @PostMapping("/updateAgentDomain")
  @PreAuthorize("hasAuthority('diffusion:agnetDomain:edit')")
  public void updateAgentDomain(@RequestBody AgentDomainDTO domainDTO) {
    domainService.updateAgentDomain(domainDTO);
  }

  @ApiOperation("导出")
  @GetMapping("/exportList")
  @PreAuthorize("hasAuthority('diffusion:agnetDomain:export')")
  public void exportList(AgentDomainDTO domainDTO, HttpServletResponse response) {
    domainService.exportList(domainDTO, response);
  }
}
