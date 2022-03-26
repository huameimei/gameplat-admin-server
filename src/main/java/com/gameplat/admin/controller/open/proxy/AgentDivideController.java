package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.vo.AgentPlanVO;
import com.gameplat.admin.service.AgentDivideService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "代理账号的平级分红方案")
@Slf4j
@RestController
@RequestMapping("/api/admin/same-level/agent-divide")
public class AgentDivideController {
  @Autowired private AgentDivideService agentDivideService;

  @ApiOperation(value = "查询可选代理账号列表")
  @GetMapping("/optionalList")
  public IPage<AgentPlanVO> optionalList(
      PageDTO<AgentPlanVO> page, @RequestParam(required = false) String agentName) {
    Integer divideType = 0;
    return agentDivideService.queryPage(page, divideType, agentName);
  }

  @ApiOperation(value = "查询已绑定代理账号列表")
  @GetMapping("/boundList")
  public IPage<AgentPlanVO> boundAgentPlanList(
      PageDTO<AgentPlanVO> page, @RequestParam(required = false) String agentName) {
    Integer divideType = 1;
    return agentDivideService.queryPage(page, divideType, agentName);
  }


  @ApiOperation("代理线绑定平级分红方案")
  @PostMapping(value = "/bindAgentLine")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.AGENT,
      desc = "代理线绑定平级分红方案，代理线：#{#familyIds}")
  public void bindAgentLine(@RequestParam String familyIds) {
    log.info("代理线绑定佣金方案：familyIds={}", familyIds);
    Integer divideType = 1;
    agentDivideService.bindAgentLineDivide(divideType, familyIds);
  }

  @ApiOperation("代理绑定平级分红方案")
  @PostMapping(value = "/bindAgent")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.AGENT,
      desc = "代理绑定平级分红方案，代理账号ID：#{#agentId}，旧方案ID：#{#oldPlanId}，新方案ID：#{#planId}，备注信息：#{#remark}")
  public void bindAgent(
      @RequestParam Long oldPlanId, @RequestParam Long planId, @RequestParam Long agentId) {
    log.info("代理绑定佣金方案：oldPlanId={}, planId={}, agentId={}", oldPlanId, planId, agentId);
    Integer divideType = 1;
    agentDivideService.bindAgentDivide(divideType, planId, agentId);
  }
}
