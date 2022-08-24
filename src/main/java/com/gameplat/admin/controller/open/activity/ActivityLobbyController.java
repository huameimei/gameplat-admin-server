package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.ActivityLobbyAddDTO;
import com.gameplat.admin.model.dto.ActivityLobbyQueryDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.admin.model.vo.CodeDataVO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.DateUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.ActivityInfoEnum;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.activity.ActivityLobby;
import com.gameplat.model.entity.activity.ActivityTurntable;
import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户活动大厅
 *
 * @author kenvin
 */
@Tag(name = "活动大厅管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/lobby")
public class ActivityLobbyController {

  @Autowired private ActivityLobbyService activityLobbyService;

  @Autowired private SysDictDataService sysDictDataService;

  @Autowired private GameKindService gameKindService;

  @Autowired private ActivityInfoService activityInfoService;

  @Autowired private ActivityTurntableService activityTurntableService;

  @Operation(summary = "活动大厅列表")
  @GetMapping("/list")
  //  @PreAuthorize("hasAuthority('activity:lobby:view')")
  public IPage<ActivityLobbyVO> list(
      @Parameter(hidden = true) PageDTO<ActivityLobby> page, ActivityLobbyQueryDTO dto) {
    return activityLobbyService.findActivityLobbyList(page, dto);
  }

  @Operation(summary = "新增活动大厅")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('activity:lobby:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动分发管理->新增活动大厅ids:' + #map.ids")
  public void add(@Validated @RequestBody ActivityLobbyAddDTO dto) {
    if (StringUtils.isNull(dto.getStatisDate())) {
      throw new ServiceException("请选择统计日期");
    }
    if (dto.getApplyWay() == ActivityInfoEnum.ApplyWay.AUTOMATIC.value()
        && dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.NO.value()) {
      throw new ServiceException("自动申请的活动必须勾选隔天申请");
    }
    if (dto.getApplyWay() == ActivityInfoEnum.ApplyWay.AUTOMATIC.value()
            && dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.NO.value()
            && dto.getStatisItem() != ActivityInfoEnum.StatisItem.SINGLE_DAY_DEPOSIT_AMOUNT.getValue()
            && dto.getStatisItem() != ActivityInfoEnum.StatisItem.FIRST_DEPOSIT_AMOUNT.getValue()
            && dto.getStatisItem() != ActivityInfoEnum.StatisItem.SINGLE_DAY_TWO_DEPOSIT_AMOUNT.getValue()
            && dto.getStatisItem() != ActivityInfoEnum.StatisItem.TWO_DEPOSIT_AMOUNT.getValue() ) {
      throw new ServiceException("自动申请的活动除了首充和二充活动，其他的必须勾选隔天申请");
    }
    if (dto.getEndTime().before(dto.getStartTime())) {
      throw new ServiceException("活动结束时间不能小于活动开始时间");
    }
    //如果奖励计算类型为2，则为百分比计算，天数相关的活动暂不支持百分比计算
    if(dto.getRewardCalculateType() == ActivityInfoEnum.RewardCalculateTypeEnum.PERCENTAGE_AMOUNT.value()){
      if(dto.getStatisItem() == ActivityInfoEnum.StatisItem.CONTINUOUS_RECHARGE_DAYS.getValue()
              || dto.getStatisItem() == ActivityInfoEnum.StatisItem.CUMULATIVE_GAME_DML_DAYS.getValue()
              || dto.getStatisItem() == ActivityInfoEnum.StatisItem.CONSECUTIVE_GAME_DML_DAYS.getValue()) {
        throw new ServiceException("天数相关的活动类型不支持百分比计算");
      }
    }

    if(DateUtil.daysBetween(dto.getStartTime(), dto.getEndTime()) > 1825){
      throw new ServiceException("活动有效期的开始时间和结束时间最大不能超过5年间隔");
    }
    activityLobbyService.add(dto);
  }



  @Operation(summary = "修改活动大厅")
  @PostMapping("/update")
  @PreAuthorize("hasAuthority('activity:lobby:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动大厅管理->修改活动大厅id:' + #dto.id + 'title:' + #dto.title + 'activityType:' + dto.activityType")
  public void update(@RequestBody ActivityLobbyUpdateDTO dto) {
    if (dto.getId() == null || dto.getId() == 0) {
      throw new ServiceException("id不能为空");
    }
    if (StringUtils.isNull(dto.getStatisDate())) {
      throw new ServiceException("请选择统计日期");
    }
    if (dto.getApplyWay() == ActivityInfoEnum.ApplyWay.AUTOMATIC.value()
            && dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.NO.value()
            && dto.getStatisItem() != ActivityInfoEnum.StatisItem.SINGLE_DAY_DEPOSIT_AMOUNT.getValue()
            && dto.getStatisItem() != ActivityInfoEnum.StatisItem.FIRST_DEPOSIT_AMOUNT.getValue()
            && dto.getStatisItem() != ActivityInfoEnum.StatisItem.SINGLE_DAY_TWO_DEPOSIT_AMOUNT.getValue()
            && dto.getStatisItem() != ActivityInfoEnum.StatisItem.TWO_DEPOSIT_AMOUNT.getValue() ) {
      throw new ServiceException("自动申请的活动除了首充和二充活动，其他的必须勾选隔天申请");
    }
    if (dto.getEndTime().before(dto.getStartTime())) {
      throw new ServiceException("活动结束时间不能小于活动开始时间");
    }
    //如果奖励计算类型为2，则为百分比计算，天数相关的活动暂不支持百分比计算
    if(dto.getRewardCalculateType() == ActivityInfoEnum.RewardCalculateTypeEnum.PERCENTAGE_AMOUNT.value()){
      if(dto.getStatisItem() == ActivityInfoEnum.StatisItem.CONTINUOUS_RECHARGE_DAYS.getValue()
              || dto.getStatisItem() == ActivityInfoEnum.StatisItem.CUMULATIVE_GAME_DML_DAYS.getValue()
              || dto.getStatisItem() == ActivityInfoEnum.StatisItem.CONSECUTIVE_GAME_DML_DAYS.getValue()) {
        throw new ServiceException("天数相关的活动类型不支持百分比计算");
      }
    }

    if(DateUtil.daysBetween(dto.getStartTime(), dto.getEndTime()) > 1825){
      throw new ServiceException("活动有效期的开始时间和结束时间最大不能超过5年间隔");
    }
    activityLobbyService.update(dto);
  }

  @Operation(summary = "删除活动大厅")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('activity:lobby:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动大厅管理->删除活动大厅ids:' + #map.id")
  public void remove(@RequestBody Map<String, String> map) {
    if (StringUtils.isBlank(map.get("ids"))) {
      throw new ServiceException("ids不能为空");
    }
    activityLobbyService.remove(map.get("ids"));
  }

  @Operation(summary = "更新活动大厅状态")
  @PostMapping("/updateStatus")
  @PreAuthorize("hasAuthority('activity:lobby:updateStatus')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动大厅管理->更新活动大厅状态id:' + #dto.id + 'status' + #dto.status")
  public void updateStatus(@RequestBody ActivityLobbyUpdateStatusDTO dto) {
    if (dto.getId() == null || dto.getId() == 0) {
      throw new ServiceException("id不能为空");
    }
    activityLobbyService.updateStatus(dto);
  }

  @Operation(summary = "查询未绑定的活动大厅列表")
  @GetMapping("/findUnboundLobbyList")
  @PreAuthorize("hasAuthority('activity:lobby:findUnboundLobbyList')")
  public List<ActivityLobbyVO> findUnboundLobbyList() {
    return activityInfoService.findUnboundLobbyList(false);
  }

  @Operation(summary = "查询所有的活动大厅列表")
  @GetMapping("/findAllLobbyList")
  @PreAuthorize("hasAuthority('activity:lobby:findAllLobbyList')")
  public List<ActivityLobbyVO> findAllLobbyList() {
    return activityLobbyService.findAllLobbyList();
  }

  @Operation(summary = "游戏类型列表")
  @GetMapping("/gameTypeList")
  @PreAuthorize("hasAuthority('activity:lobby:gameTypeList')")
  public List<CodeDataVO> gameTypeList() {
    SysDictData dictData = new SysDictData();
    dictData.setDictType(DictTypeEnum.LIVE_GAME_TYPE.getValue());
    dictData.setStatus(EnableEnum.ENABLED.code());
    List<SysDictData> dictList = sysDictDataService.getDictList(dictData);
    if (CollectionUtils.isEmpty(dictList)) {
      throw new ServiceException("游戏类型列表没有配置");
    }

    List<CodeDataVO> list = new ArrayList<>();
    CodeDataVO codeDataVO;
    for (SysDictData data : dictList) {
      codeDataVO = new CodeDataVO();
      codeDataVO.setCode(data.getDictValue());
      codeDataVO.setName(data.getDictLabel());
      list.add(codeDataVO);
    }
    return list;
  }

  @Operation(summary = "游戏列表")
  @GetMapping("/gameList")
  @PreAuthorize("hasAuthority('activity:lobby:gameList')")
  public List<GameKindVO> getGameKindInBanner(String gameTypeCode) {
    return gameKindService.getGameKindInBanner(gameTypeCode);
  }

  /**
   * 活动详情
   *
   * @param id Long
   * @return ActivityLobbyVO
   */
  @Operation(summary = "活动详情")
  @GetMapping("/detail")
  public ActivityLobbyVO detail(Long id) {
    if (id == null || id == 0) {
      throw new ServiceException("活动id不能为空");
    }
    return activityLobbyService.getActivityLobbyVOById(id);
  }

  /**
   * 查询转盘活动详情
   * @param id
   * @return ActivityTurntable
   */
  @Operation(summary = "查询转盘活动详情")
  @GetMapping("/getActivityTurntableDetail")
  @PreAuthorize("hasAuthority('activity:lobby:getActivityTurntableDetail')")
  public ActivityTurntable getActivityTurntableDetail(Integer id) {
    if (id == null || id <= 0) {
      throw new ServiceException("转盘活动id不能为空");
    }
    return activityTurntableService.getById(id);
  }

  /**
   * 查询转盘活动列表
   * @param page
   * @param dto
   * @return
   */
  @Operation(summary = "查询转盘活动列表")
  @GetMapping("/getActivityTurntableList")
  @PreAuthorize("hasAuthority('activity:lobby:getActivityTurntableList')")
  public IPage<ActivityTurntable> getActivityTurntableList(
          @Parameter(hidden = true) PageDTO<ActivityTurntable> page, ActivityTurntable dto,String startDate,String endDate) {
    return activityTurntableService.findActivityTurntableList(page, dto,startDate,endDate);
  }

  /**
   * 更新转盘信息
   * @param bean
   * @return
   */
  @Operation(summary = "新增或更新转盘活动详情")
  @PostMapping("/addActivityTurntable")
  @PreAuthorize("hasAuthority('activity:lobby:addActivityTurntable')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动大厅管理->新增或更新转盘活动详情bean:' + #bean")
  public boolean addActivityTurntable(@RequestBody ActivityTurntable bean) {
    return activityTurntableService.addActivityTurntable(bean);
  }

  @Operation(summary = "删除转盘活动")
  @PostMapping("/removeActivityTurntable")
  @PreAuthorize("hasAuthority('activity:info:removeActivityTurntable')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动大厅管理->删除转盘活动ids:' + #ids")
  public void removeActivityTurntable(String ids) {
    Assert.notNull(ids, "id不能为空！");
    activityTurntableService.delete(ids);
  }
}
