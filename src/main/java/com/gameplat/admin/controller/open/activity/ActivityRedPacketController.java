package com.gameplat.admin.controller.open.activity;

import com.gameplat.admin.model.dto.ActivityRedPacketConfigDTO;
import com.gameplat.admin.model.dto.ActivityTurntablePrizeConfigDTO;
import com.gameplat.admin.model.vo.ActivityRedPacketConfigVO;
import com.gameplat.admin.model.vo.ActivityTurntablePrizeConfigVO;
import com.gameplat.admin.service.ActivityRedPacketService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动红包配置
 *
 * @author kenvin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/redpacket")
@Tag(name = "活动红包配置")
public class ActivityRedPacketController {

  @Autowired private ActivityRedPacketService activityRedPacketService;

  @Operation(summary = "获取红包配置")
  @GetMapping("/getConfig")
  @PreAuthorize("hasAuthority('activity:redpacket:getConfig')")
  public ActivityRedPacketConfigVO getConfig() {
    return activityRedPacketService.getConfig();
  }

  @Operation(summary = "更新红包配置")
  @PostMapping("/updateConfig")
  @PreAuthorize("hasAuthority('activity:redpacket:updateConfig')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动红包配置->更新红包配置redenvelopeId:' + #dto.redenvelopeId")
  public void updateConfig(@RequestBody ActivityRedPacketConfigDTO dto) {
    activityRedPacketService.updateConfig(dto);
  }

  @Operation(summary = "获取转盘奖品配置")
  @GetMapping("/getTurntablePrizeConfig")
  @PreAuthorize("hasAuthority('activity:redpacket:getTurntablePrizeConfig')")
  public List<ActivityTurntablePrizeConfigVO> getTurntablePrizeConfig() {
    return activityRedPacketService.getTurntablePrizeConfig();
  }

  @Operation(summary = "更新转盘奖品配置")
  @PostMapping("/updateTurntablePrizeConfig")
  @PreAuthorize("hasAuthority('activity:redpacket:updateTurntablePrizeConfig')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动红包配置->更新红包配置:' + #dto")
  public void updateTurntablePrizeConfig(@RequestBody ActivityTurntablePrizeConfigDTO dto) {
    if (dto.getPrizeId() == null) {
      throw new ServiceException("奖品ID不能为空");
    }
    activityRedPacketService.updateTurntablePrizeConfig(dto);
  }
}
