package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.ActivityBlacklistAddDTO;
import com.gameplat.admin.model.dto.ActivityBlacklistQueryDTO;
import com.gameplat.admin.service.ActivityBlacklistService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.activity.ActivityBlacklist;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 活动黑名单管理
 *
 * @author kenvin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/blacklist")
@Tag(name = "活动黑名单管理")
public class ActivityBlacklistController {

  @Autowired private ActivityBlacklistService activityBlacklistService;

  @Operation(summary = "活动黑名单列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('account:activityBlack:view')")
  public IPage<ActivityBlacklist> list(
      @Parameter(hidden = true) PageDTO<ActivityBlacklist> page, ActivityBlacklistQueryDTO dto) {
    return activityBlacklistService.list(page, dto);
  }

  @Operation(summary = "新增活动黑名单")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('account:activityBlack:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动黑名单管理->新增活动黑名单limitedContent:' + #dto.limitedContent + 'limitedType:' + #dto.limitedType")
  public void add(@Validated @RequestBody ActivityBlacklistAddDTO dto) {
    activityBlacklistService.add(dto);
  }

  @Operation(summary = "删除活动黑名单")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('account:activityBlack:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动黑名单管理->删除活动黑名单ids:' + #ids")
  public void remove(String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("ids不能为空");
    }
    activityBlacklistService.remove(ids);
  }
}
