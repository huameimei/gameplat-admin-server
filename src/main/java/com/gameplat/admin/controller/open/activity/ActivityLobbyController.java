package com.gameplat.admin.controller.open.activity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.ActivityInfoEnum;
import com.gameplat.admin.model.dto.ActivityLobbyAddDTO;
import com.gameplat.admin.model.dto.ActivityLobbyQueryDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.admin.model.vo.CodeDataVO;
import com.gameplat.admin.model.vo.GameKindVO;
import com.gameplat.admin.service.ActivityInfoService;
import com.gameplat.admin.service.ActivityLobbyService;
import com.gameplat.admin.service.GameKindService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.base.common.enums.EnableEnum;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.DictTypeEnum;
import com.gameplat.model.entity.activity.ActivityLobby;
import com.gameplat.model.entity.sys.SysDictData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户活动大厅
 *
 * @author kenvin
 */
@Api(tags = "活动大厅管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/activity/lobby")
public class ActivityLobbyController {

  @Autowired private ActivityLobbyService activityLobbyService;

  @Autowired private SysDictDataService sysDictDataService;

  @Autowired private GameKindService gameKindService;

  @Autowired private ActivityInfoService activityInfoService;

  @ApiOperation(value = "活动大厅列表")
  @GetMapping("/list")
//  @PreAuthorize("hasAuthority('activity:lobby:view')")
  public IPage<ActivityLobbyVO> list(
      @ApiIgnore PageDTO<ActivityLobby> page, ActivityLobbyQueryDTO dto) {
    return activityLobbyService.findActivityLobbyList(page, dto);
  }

  @ApiOperation(value = "新增活动大厅")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('activity:lobby:add')")
  public void add(@Validated @RequestBody ActivityLobbyAddDTO dto) {
    if (StringUtils.isNull(dto.getStatisDate())) {
      throw new ServiceException("请选择统计日期");
    }
    if (dto.getApplyWay() == ActivityInfoEnum.ApplyWay.AUTOMATIC.value()
        && dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.NO.value()) {
      throw new ServiceException("自动申请的活动必须勾选隔天申请");
    }
    if (dto.getEndTime().before(dto.getStartTime())) {
      throw new ServiceException("活动结束时间不能小于活动开始时间");
    }
    activityLobbyService.add(dto);
  }

  @ApiOperation(value = "修改活动大厅")
  @PostMapping("/update")
  @PreAuthorize("hasAuthority('activity:lobby:edit')")
  public void update(@RequestBody ActivityLobbyUpdateDTO dto) {
    if (dto.getId() == null || dto.getId() == 0) {
      throw new ServiceException("id不能为空");
    }
    if (StringUtils.isNull(dto.getStatisDate())) {
      throw new ServiceException("请选择统计日期");
    }
    if (dto.getApplyWay() == ActivityInfoEnum.ApplyWay.AUTOMATIC.value()
        && dto.getNextDayApply() == ActivityInfoEnum.NextDayApply.NO.value()) {
      throw new ServiceException("自动申请的活动必须勾选隔天申请");
    }
    if (dto.getEndTime().before(dto.getStartTime())) {
      throw new ServiceException("活动结束时间不能小于活动开始时间");
    }
    activityLobbyService.update(dto);
  }

  @ApiOperation(value = "删除活动大厅")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('activity:lobby:remove')")
  public void remove(String ids) {
    if (StringUtils.isBlank(ids)) {
      throw new ServiceException("ids不能为空");
    }
    activityLobbyService.remove(ids);
  }

  @ApiOperation(value = "更新活动大厅状态")
  @PostMapping("/updateStatus")
  @PreAuthorize("hasAuthority('activity:lobby:updateStatus')")
  public void updateStatus(@RequestBody ActivityLobbyUpdateStatusDTO dto) {
    if (dto.getId() == null || dto.getId() == 0) {
      throw new ServiceException("id不能为空");
    }
    activityLobbyService.updateStatus(dto);
  }

  @ApiOperation(value = "查询未绑定的活动大厅列表")
  @GetMapping("/findUnboundLobbyList")
  @PreAuthorize("hasAuthority('activity:lobby:findUnboundLobbyList')")
  public List<ActivityLobbyVO> findUnboundLobbyList() {
    return activityInfoService.findUnboundLobbyList(false);
  }

  @ApiOperation(value = "查询所有的活动大厅列表")
  @GetMapping("/findAllLobbyList")
  @PreAuthorize("hasAuthority('activity:lobby:findAllLobbyList')")
  public List<ActivityLobbyVO> findAllLobbyList() {
    return activityLobbyService.findAllLobbyList();
  }

  @ApiOperation(value = "游戏类型列表")
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

  @ApiOperation(value = "游戏列表")
  @GetMapping("/gameList")
  @PreAuthorize("hasAuthority('activity:lobby:gameList')")
  public List<GameKindVO> getGameKindInBanner(String gameTypeCode) {
    return gameKindService.getGameKindInBanner(gameTypeCode);
  }
}
