package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.dto.ActivityDistributeQueryDTO;
import com.gameplat.admin.model.vo.ActivityDistributeStatisticsVO;
import com.gameplat.admin.model.vo.ActivityDistributeVO;
import com.gameplat.admin.service.ActivityDistributeService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.activity.ActivityDistribute;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 活动分发管理
 *
 * @author kenvin
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/activity/distribute")
@Tag(name = "活动分发管理")
public class ActivityDistributeController {

  @Autowired private ActivityDistributeService activityDistributeService;

  @Operation(summary = "活动分发列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('activity:distribute:view')")
  public PageExt<IPage<ActivityDistributeVO>, ActivityDistributeStatisticsVO> list(
      @Parameter(hidden = true) PageDTO<ActivityDistribute> page, ActivityDistributeQueryDTO dto) {
    return activityDistributeService.list(page, dto);
  }

  @Operation(summary = "修改结算状态")
  @PostMapping("/updateStatus")
  @PreAuthorize("hasAuthority('activity:distribute:updateStatus')")
  public void updateStatus(@RequestBody String ids) {
    activityDistributeService.updateStatus(ids);
  }

  @Operation(summary = "删除分发")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('activity:distribute:remove')")
  public void remove(@RequestBody Map<String, String> map) {
    if (StringUtils.isBlank(map.get("ids"))) {
      throw new ServiceException("删除活动分发时，ids不能为空");
    }
    activityDistributeService.updateDeleteStatus(map.get("ids"));
  }
}
