package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.AgentDomainDTO;
import com.gameplat.admin.model.vo.AgentDomainVO;
import com.gameplat.admin.service.AgentDomainService;
import com.gameplat.model.entity.spread.AgentDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequestMapping("/api/admin/operator/diffusion/agent")
public class AgentDomainController {

  @Autowired private AgentDomainService domainService;


    /**
   * 代理域名列表
   */
  @GetMapping("/agentDomainList")
  public IPage<AgentDomainVO> agentDomainList(PageDTO<AgentDomain> page, AgentDomainDTO domainDTO) {
    return domainService.agentDomainList(page,domainDTO);
  }


  /**
   *  新增代理域名
   */
  @PostMapping("/createAgentDomain")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:add')")
  public void createAgentDomain(@RequestBody AgentDomainDTO domainDTO) {
    domainService.createAgentDomain(domainDTO);
  }


    /**
   * 删除代理域名
   */
  @PostMapping("/deleteAgentDomain")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:add')")
  public void deleteAgentDomain(@RequestParam Integer id) {
    domainService.deleteAgentDomain(id);
  }


  /**
   * 修改代理域名
   */
  @PostMapping("/updateAgentDomain")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:add')")
  public void updateAgentDomain(@RequestBody AgentDomainDTO domainDTO) {
    domainService.updateAgentDomain(domainDTO);
  }

}
