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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 活动分发管理
 *
 * @author kenvin
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/activity/distribute")
@Api(tags = "活动分发管理")
public class ActivityDistributeController {

  @Autowired private ActivityDistributeService activityDistributeService;

  @ApiOperation(value = "活动分发列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('activity:distribute:view')")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
    @ApiImplicitParam(name = "size", value = "每页条数"),
  })
  public PageExt<IPage<ActivityDistributeVO>, ActivityDistributeStatisticsVO> list(
      @ApiIgnore PageDTO<ActivityDistribute> page, ActivityDistributeQueryDTO dto) {
    return activityDistributeService.list(page, dto);
  }

  @ApiOperation(value = "修改结算状态")
  @PutMapping("/updateStatus")
  @PreAuthorize("hasAuthority('activity:distribute:updateStatus')")
  public void updateStatus(@RequestBody String ids) {
    activityDistributeService.updateStatus(ids);
  }

  @ApiOperation(value = "删除分发")
  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('activity:distribute:remove')")
  public void remove(@RequestBody String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("删除活动分发时，ids不能为空");
    }
    activityDistributeService.updateDeleteStatus(ids);
  }
}
