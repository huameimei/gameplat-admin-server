package com.gameplat.admin.controller.open.activity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.ActivityInfoEnum;
import com.gameplat.admin.model.dto.ActivityInfoAddDTO;
import com.gameplat.admin.model.dto.ActivityInfoQueryDTO;
import com.gameplat.admin.model.dto.ActivityInfoUpdateDTO;
import com.gameplat.admin.model.dto.ActivityInfoUpdateSortDTO;
import com.gameplat.admin.model.vo.ActivityInfoVO;
import com.gameplat.admin.model.vo.ValueDataVO;
import com.gameplat.admin.service.ActivityInfoService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.model.entity.activity.ActivityInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理
 *
 * @author kenvin
 */
@Slf4j
@Api(tags = "活动发布管理")
@Validated
@RestController
@RequestMapping("/api/admin/activity/info")
public class ActivityInfoController {

  @Autowired private ActivityInfoService activityInfoService;

  @Autowired private ConfigService configService;

  /**
   * 活动列表
   *
   * @param page PageDTO
   * @param dto ActivityInfoQueryDTO
   * @return IPage
   */
  @ApiOperation(value = "活动列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('activity:info:page')")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "current", value = "分页参数：当前页", defaultValue = "1"),
    @ApiImplicitParam(name = "size", value = "每页条数")
  })
  public IPage<ActivityInfoVO> list(
      @ApiIgnore PageDTO<ActivityInfo> page, ActivityInfoQueryDTO dto) {
    return activityInfoService.list(page, dto);
  }

  /**
   * 活动详情
   *
   * @param id Long
   * @return ActivityInfoVO
   */
  @ApiOperation(value = "活动详情")
  @GetMapping("/detail")
  @PreAuthorize("hasAuthority('activity:info:detail')")
  public ActivityInfoVO detail(Long id) {
    if (id == null || id == 0) {
      throw new ServiceException("活动id不能为空");
    }
    return activityInfoService.detail(id);
  }

  @ApiOperation(value = "新增活动")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('activity:info:add')")
  public void add(@RequestBody ActivityInfoAddDTO dto) {
    checkActivityInfo(
        dto.getValidStatus(),
        dto.getEndTime(),
        dto.getBeginTime(),
        dto.getActivityLobbyId(),
        dto.getId());
    activityInfoService.add(dto);
  }

  /**
   * 编辑活动
   *
   * @param dto ActivityInfoUpdateDTO
   */
  @ApiOperation(value = "编辑活动")
  @PostMapping("/update")
  @PreAuthorize("hasAuthority('activity:info:update')")
  public void update(@RequestBody ActivityInfoUpdateDTO dto) {
    checkActivityInfo(
        dto.getValidStatus(),
        dto.getEndTime(),
        dto.getBeginTime(),
        dto.getActivityLobbyId(),
        dto.getId());
    activityInfoService.update(dto);
  }

  /**
   * 修改活动排序
   *
   * @param dto ActivityInfoUpdateSortDTO
   */
  @ApiOperation(value = "修改活动排序")
  @PostMapping("/updateSort")
  @PreAuthorize("hasAuthority('activity:info:updateSort')")
  public void updateSort(@Validated @RequestBody ActivityInfoUpdateSortDTO dto) {
    activityInfoService.updateSort(dto);
  }

  /**
   * 删除活动
   *
   * @param ids String
   */
  @ApiOperation(value = "删除活动")
  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('activity:info:remove')")
  public void delete(@RequestBody @NotEmpty(message = "缺少参数") String ids) {
    activityInfoService.delete(ids);
  }

  /**
   * 获取全部活动
   *
   * @return List
   */
  @ApiOperation(value = "获取全部活动")
  @GetMapping("/getAllActivity")
  @PreAuthorize("hasAuthority('activity:info:getAllActivity')")
  public List<ActivityInfoVO> getAllActivity() {
    List<ActivityInfoVO> activityList = activityInfoService.getAllActivity();
    if (CollectionUtils.isEmpty(activityList)) {
      return new ArrayList<>();
    }
    ArrayList<ActivityInfoVO> result = new ArrayList<>();
    long nowTime = System.currentTimeMillis();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    activityList.forEach(
        o -> {
          if (o.getValidStatus() == ActivityInfoEnum.ValidStatus.PERMANENT.value()) {
            result.add(o);
          } else if (o.getValidStatus() == ActivityInfoEnum.ValidStatus.TIME_LIMIT.value()) {
            String beginDate = o.getBeginTime().concat(" 00:00:00");
            String endDate = o.getEndTime().concat(" 23:59:59");
            try {
              if (nowTime > sdf.parse(beginDate).getTime()
                  && nowTime < sdf.parse(endDate).getTime()) {
                result.add(o);
              }
            } catch (ParseException e) {
              log.info("获取关联了活动规则的全部活动信息,时间转换报错，原因{}", e.getMessage());
            }
          }
        });
    return result;
  }

  /**
   * 活动排序值列表
   *
   * @return List
   */
  @ApiOperation(value = "活动排序值列表")
  @GetMapping("/sortList")
  @PreAuthorize("hasAuthority('activity:info:sortList')")
  public List<ValueDataVO> sortList() {
    String activitySortConfig = configService.getValue(DictDataEnum.ACTIVITY_SORT_CONFIG);
    if (StringUtils.isBlank(activitySortConfig)) {
      throw new ServiceException("活动排序值没有配置");
    }
    return JSONArray.parseArray(activitySortConfig, ValueDataVO.class);
  }

  /**
   * 检查活动是否满足条件
   *
   * @param validStatus Integer
   * @param endTime String
   * @param beginTime String
   * @param activityLobbyId Long
   * @param id Long
   */
  private void checkActivityInfo(
      Integer validStatus, String endTime, String beginTime, Long activityLobbyId, Long id) {
    if (validStatus == ActivityInfoEnum.ValidStatus.TIME_LIMIT.value()) {
      if (DateUtil.strToDate(endTime, "yyyy-MM-dd")
          .before(DateUtil.strToDate(beginTime, "yyyy-MM-dd"))) {
        throw new ServiceException("活动结束时间不能小于活动开始时间");
      }
    }
    if (StringUtils.isNotNull(activityLobbyId)) {
      activityInfoService.checkActivityLobbyId(activityLobbyId, id);
    }
  }
}
