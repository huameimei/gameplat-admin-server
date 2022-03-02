package com.gameplat.admin.controller.open.activity;

import com.gameplat.admin.model.dto.ActivityRedPacketConfigDTO;
import com.gameplat.admin.model.dto.ActivityTurntablePrizeConfigDTO;
import com.gameplat.admin.model.vo.ActivityRedPacketConfigVO;
import com.gameplat.admin.model.vo.ActivityTurntablePrizeConfigVO;
import com.gameplat.admin.service.ActivityRedPacketService;
import com.gameplat.base.common.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "活动红包配置")
public class ActivityRedPacketController {

  @Autowired private ActivityRedPacketService activityRedPacketService;

  /**
   * 获取红包配置
   *
   * @return
   */
  @ApiOperation(value = "获取红包配置")
  @GetMapping("/getConfig")
  @PreAuthorize("hasAuthority('activity:redpacket:getConfig')")
  public ActivityRedPacketConfigVO getConfig() {
    return activityRedPacketService.getConfig();
  }

  /**
   * 更新红包配置
   *
   * @return
   */
  @ApiOperation(value = "更新红包配置")
  @PutMapping("/updateConfig")
  @PreAuthorize("hasAuthority('activity:redpacket:updateConfig')")
  public void updateConfig(@RequestBody ActivityRedPacketConfigDTO activityRedPacketConfigDTO) {
    activityRedPacketService.updateConfig(activityRedPacketConfigDTO);
  }

  /**
   * 获取转盘奖品配置
   *
   * @return
   */
  @ApiOperation(value = "获取转盘奖品配置")
  @GetMapping("/getTurntablePrizeConfig")
  @PreAuthorize("hasAuthority('activity:redpacket:getTurntablePrizeConfig')")
  public List<ActivityTurntablePrizeConfigVO> getTurntablePrizeConfig() {
    return activityRedPacketService.getTurntablePrizeConfig();
  }

  /**
   * 更新转盘奖品配置
   *
   * @return
   */
  @ApiOperation(value = "更新转盘奖品配置")
  @PutMapping("/updateTurntablePrizeConfig")
  @PreAuthorize("hasAuthority('activity:redpacket:updateTurntablePrizeConfig')")
  public void updateTurntablePrizeConfig(
      @RequestBody ActivityTurntablePrizeConfigDTO activityTurntablePrizeConfigDTO) {
    if (activityTurntablePrizeConfigDTO.getPrizeId() == null) {
      throw new ServiceException("奖品ID不能为空");
    }
    activityRedPacketService.updateTurntablePrizeConfig(activityTurntablePrizeConfigDTO);
  }
}
