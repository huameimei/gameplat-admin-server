package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.ActivityQualificationVO;
import com.gameplat.admin.service.ActivityQualificationService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.activity.ActivityQualification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.Map;

/**
 * 活动资格管理
 *
 * @author kenvin
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/qualification")
@Tag(name = "活动资格管理")
public class ActivityQualificationController {

  @Autowired private ActivityQualificationService activityQualificationService;

  @Operation(summary = "活动资格列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('activity:qualification:view')")
  public IPage<ActivityQualificationVO> list(
      @Parameter(hidden = true) PageDTO<ActivityQualification> page,
      ActivityQualificationQueryDTO dto) {
    return activityQualificationService.list(page, dto);
  }

  @Operation(summary = "新增活动资格")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('activity:qualification:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动资格管理->新增活动资格username:' + #dto.username + 'type' + #dto.type")
  public void add(@RequestBody ActivityQualificationAddDTO dto) {
    activityQualificationService.add(dto);
  }

  @Operation(summary = "批量审核活动资格")
  @PostMapping("/auditStatus")
  @PreAuthorize("hasAuthority('activity:qualification:auditStatus')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动资格管理->批量审核活动资格idList:' + #dto.idList")
  public void auditStatus(@RequestBody ActivityQualificationAuditStatusDTO dto) {
    if (CollectionUtils.isEmpty(dto.getIdList())) {
      throw new ServiceException("id不能为空");
    }
    activityQualificationService.auditStatus(dto);
  }

  @Operation(summary = "更新活动资格状态")
  @PostMapping("/updateQualificationStatus")
  @PreAuthorize("hasAuthority('activity:qualification:updateQualificationStatus')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动资格管理->批量审核活动资格idList:' + #dto.idList")
  public void updateQualificationStatus(@RequestBody ActivityQualificationUpdateStatusDTO dto) {
    activityQualificationService.updateQualificationStatus(dto);
  }

  @Operation(summary = "删除活动资格")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('activity:qualification:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动资格管理->删除活动资格ids:' + #map.ids")
  public void delete(@RequestBody Map<String, String> map) {
    if (StringUtils.isEmpty(map.get("ids"))) {
      throw new ServiceException("ids 不能为空");
    }
    activityQualificationService.delete(map.get("ids"));
  }

  @Operation(summary = "资格检测")
  @PostMapping("/checkQualification")
  @PreAuthorize("hasAuthority('activity:qualification:checkQualification')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动资格管理->资格检测:' + #dto")
  public Map<String, Object> checkQualification(@RequestBody ActivityQualificationCheckDTO dto) {
    return activityQualificationService.checkQualification(dto);
  }

  /**
   * 批量生成红包资格
   *
   * @return
   */
  @Operation(summary = "手工生成红包雨资格")
  @RequestMapping(value = "/addRedPacketQualification", method = RequestMethod.POST)
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "活动资格管理->手工生成红包雨资格")
  public void addRedPacketQualification() {
    if (LocalTime.now().isBefore(LocalTime.parse("13:00"))) {
      throw new ServiceException("不在允许操作时间范围之内,请在13点以后生成资格");
    }
    activityQualificationService.activityRedEnvelopeQualification();
  }

  @Operation(summary = "批量拒绝活动资格")
  @PostMapping("/refuse")
  @PreAuthorize("hasAuthority('activity:qualification:refuse')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动资格管理->批量拒绝活动资格dto:' + #dto")
  public void refuse(@RequestBody ActivityQualificationRefuseDTO dto) {
    if (CollectionUtils.isEmpty(dto.getIdList())) {
      throw new ServiceException("id不能为空");
    }
    if (ObjectUtils.isEmpty(dto.getRefuseReason())) {
      throw new ServiceException("请填写拒绝理由");
    }
    activityQualificationService.refuse(dto);
  }
}
