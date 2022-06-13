package com.gameplat.admin.controller.open.proxy;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.vo.RebatePlanVO;
import com.gameplat.admin.service.RebatePlanService;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.proxy.RebatePlan;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description : 平级分红方案 @Author : cc @Date : 2022/4/2
 */
@Tag(name = "平级分红方案")
@Slf4j
@RestController
@RequestMapping("/api/admin/same-level/plan")
public class RebatePlanController {

  @Autowired private RebatePlanService rebatePlanService;

  /**
   * 分页列表
   *
   * @param page
   * @param dto
   * @return
   */
  @Operation(summary = "查询平级分红方案列表")
  @GetMapping("/list")
  public IPage<RebatePlanVO> list(PageDTO<RebatePlan> page, RebatePlan dto) {
    return rebatePlanService.queryPage(page, dto);
  }

  /**
   * 新增
   *
   * @param rebatePlanPO
   */
  @Operation(summary = "平级分红->新增平级分红方案")
  @PostMapping(value = "/add")
  @PreAuthorize("hasAuthority('system:plan:add')")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.AGENT,
      desc =
          "平级分红->新增平级分红方案，"
              + "方案名称：#{#planName}，"
              + "下级分红提成：#{#lowerCommission*100}%，"
              + "下下级分红提成：#{#subCommission*100}%，"
              + "流水返利占比：#{#turnoverCommission*100}%，"
              + "备注信息：#{#remark}")
  public void addRebatePlan(@RequestBody RebatePlan rebatePlanPO) {
    log.info("新增平级分红方案：rebatePlanPO={}", rebatePlanPO);
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rebatePlanPO.setCreateBy(userCredential.getUsername());
    rebatePlanService.addRebatePlan(rebatePlanPO);
  }

  /**
   * 编辑
   *
   * @param rebatePlanPO
   */
  @Operation(summary = "平级分红->编辑平级分红方案")
  @PostMapping(value = "/edit")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.AGENT,
      desc =
          "平级分红->编辑平级分红方案，"
              + "方案名称：#{#oldPlanName} -> #{#planName}，"
              + "下级分红提成：#{#oldLowerCommission*100}% -> #{#lowerCommission*100}%，"
              + "下下级分红提成：#{#oldSubCommission*100}% -> #{#subCommission*100}%，"
              + "流水返利占比：#{#oldTurnoverCommission*100}% -> #{#turnoverCommission*100}%，"
              + "备注信息：#{#remark}")
  public void editRebatePlan(@RequestBody RebatePlan rebatePlanPO) {
    log.info("编辑平级分红方案：rebatePlanPO={}", rebatePlanPO);
    UserCredential userCredential = SecurityUserHolder.getCredential();
    rebatePlanPO.setUpdateBy(userCredential.getUsername());
    rebatePlanService.editRebatePlan(rebatePlanPO);
  }

  /**
   * 删除
   *
   * @param map
   */
  @Operation(summary = "平级分红->删除平级分红方案")
  @PostMapping("/remove")
  @Log(
      module = ServiceName.ADMIN_SERVICE,
      type = LogType.AGENT,
      desc = "平级分红->删除平级分红方案，方案ID：#{#planId}")
  public void remove(@RequestBody Map<String, String> map) {
    Assert.isTrue(StrUtil.isNotBlank(map.get("ids")), "参数为空！");
    String[] idArr = map.get("ids").split(",");
    for (String id : idArr) {
      rebatePlanService.removeRebatePlan(Long.valueOf(id));
    }
  }
}
