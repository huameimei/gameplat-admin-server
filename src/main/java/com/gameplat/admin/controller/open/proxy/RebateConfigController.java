package com.gameplat.admin.controller.open.proxy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.vo.RebateConfigVO;
import com.gameplat.admin.service.RebateConfigService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.RebateConfig;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** @Description : 平级分红方案 @Author : cc @Date : 2022/4/2 */
@Api(tags = "平级分红方案")
@Slf4j
@RestController
@RequestMapping("/api/admin/same-level/plan-detail")
public class RebateConfigController {
  @Autowired private RebateConfigService rebateConfigService;

  /**
   * 平级分红列表
   *
   * @param page
   * @param dto
   * @return
   */
  @ApiOperation(value = "查询平级分红方案列表")
  @GetMapping("/list")
  public IPage<RebateConfigVO> list(PageDTO<RebateConfig> page, RebateConfig dto) {
    return rebateConfigService.queryPage(page, dto);
  }

  /**
   * 添加平级分红
   *
   * @param rebateConfigPO
   */
  @ApiOperation("平级分红->新增平级分红方案明细")
  @PostMapping(value = "/add")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.AGENT,
      desc =
          "平级分红->新增平级分红方案明细，"
              + "方案名称：#{#planName}，"
              + "返佣等级：#{#rebateLevel}，"
              + "公司净利润：#{#agentProfit}，"
              + "有效会员数：#{#activityMember}，"
              + "佣金比例：#{#commission*100}%，"
              + "备注：#{#remark}")
  public void addRebateConfig(@RequestBody RebateConfig rebateConfigPO) {
    log.info("新增平级分红方案明细：rebateConfigPO={}", rebateConfigPO);
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rebateConfigPO.setCreateBy(userCredential.getUsername());
    rebateConfigService.addRebateConfig(rebateConfigPO);
  }

  /**
   * 编辑平级分红
   *
   * @param rebateConfigPO
   */
  @ApiOperation("平级分红->编辑平级分红方案明细")
  @PostMapping(value = "/edit")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.AGENT,
      desc =
          "平级分红->编辑平级分红方案明细，"
              + "方案名称：#{#planName}，"
              + "返佣等级：#{#oldRebateLevel} -> #{#rebateLevel}，"
              + "公司净利润：#{#oldAgentProfit} -> #{#agentProfit}，"
              + "有效会员数：#{#oldActivityMember} -> #{#activityMember}，"
              + "佣金比例：#{#oldCommission*100}% -> #{#commission*100}%，"
              + "备注：#{#remark}")
  public void editRebateConfig(@RequestBody RebateConfig rebateConfigPO) {
    log.info("编辑平级分红方案明细：rebateConfigPO={}", rebateConfigPO);
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rebateConfigPO.setUpdateBy(userCredential.getUsername());
    rebateConfigService.editRebateConfig(rebateConfigPO);
  }

  /**
   * 删除平级分红
   *
   * @param planId
   * @param configIds
   */
  @ApiOperation("平级分红->删除平级分红方案明细")
  @PostMapping(value = "/remove")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.AGENT,
      desc = "平级分红->删除平级分红方案明细，方案ID：#{#planId}，方案明细ID：#{#configIds}")
  public void removeRebateConfig(@RequestParam Long planId, @RequestParam String configIds) {
    rebateConfigService.removeRebateConfig(configIds, planId);
  }
}
