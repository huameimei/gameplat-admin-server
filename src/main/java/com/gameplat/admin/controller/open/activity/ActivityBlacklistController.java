package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.ActivityBlacklistAddDTO;
import com.gameplat.admin.model.dto.ActivityBlacklistQueryDTO;
import com.gameplat.admin.service.ActivityBlacklistService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.activity.ActivityBlacklist;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 活动黑名单管理
 *
 * @author kenvin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/blacklist")
@Api(tags = "活动黑名单管理")
public class ActivityBlacklistController {

  @Autowired private ActivityBlacklistService activityBlacklistService;

  @ApiOperation(value = "活动黑名单列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:activityBlack:view')")
  public IPage<ActivityBlacklist> list(
      @ApiIgnore PageDTO<ActivityBlacklist> page, ActivityBlacklistQueryDTO dto) {
    return activityBlacklistService.list(page, dto);
  }

  @ApiOperation(value = "新增活动黑名单")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:activityBlack:add')")
  public void add(@Validated @RequestBody ActivityBlacklistAddDTO dto) {
    activityBlacklistService.add(dto);
  }

  @ApiOperation(value = "删除活动黑名单")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('account:activityBlack:remove')")
  public void remove(@RequestBody String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("ids不能为空");
    }
    activityBlacklistService.remove(ids);
  }
}
