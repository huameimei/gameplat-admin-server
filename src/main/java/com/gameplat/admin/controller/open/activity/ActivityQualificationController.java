package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.model.entity.activity.ActivityQualification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

/**
 * 活动资格管理
 *
 * @author kenvin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/qualification")
@Api(tags = "活动资格管理")
public class ActivityQualificationController {

  @Autowired private ActivityQualificationService activityQualificationService;

  @ApiOperation(value = "活动资格列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('activity:qualification:view')")
  public IPage<ActivityQualificationVO> list(
      @ApiIgnore PageDTO<ActivityQualification> page, ActivityQualificationQueryDTO dto) {
    return activityQualificationService.list(page, dto);
  }

  @ApiOperation(value = "新增活动资格")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('activity:qualification:add')")
  public void add(@RequestBody ActivityQualificationAddDTO dto) {
    activityQualificationService.add(dto);
  }

  @ApiOperation(value = "批量审核活动资格")
  @PutMapping("/auditStatus")
  @PreAuthorize("hasAuthority('activity:qualification:auditStatus')")
  public void auditStatus(@RequestBody ActivityQualificationAuditStatusDTO dto) {
    if (CollectionUtils.isEmpty(dto.getIdList())) {
      throw new ServiceException("id不能为空");
    }
    activityQualificationService.auditStatus(dto);
  }

  @ApiOperation(value = "更新活动资格状态")
  @PutMapping("/updateQualificationStatus")
  @PreAuthorize("hasAuthority('activity:qualification:updateQualificationStatus')")
  public void updateQualificationStatus(@RequestBody ActivityQualificationUpdateStatusDTO dto) {
    activityQualificationService.updateQualificationStatus(dto);
  }

  @ApiOperation(value = "删除活动资格")
  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('activity:qualification:remove')")
  public void delete(@RequestBody String ids) {
    activityQualificationService.delete(ids);
  }

  @ApiOperation(value = "资格检测")
  @PutMapping("/checkQualification")
  @PreAuthorize("hasAuthority('activity:qualification:checkQualification')")
  public Map<String, Object> checkQualification(@RequestBody ActivityQualificationCheckDTO dto) {
    return activityQualificationService.checkQualification(dto);
  }
}
