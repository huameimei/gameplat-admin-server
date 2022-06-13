package com.gameplat.admin.controller.open.activity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
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
import com.gameplat.common.enums.ActivityInfoEnum;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.model.entity.activity.ActivityInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 活动管理
 *
 * @author kenvin
 */
@Slf4j
@Tag(name = "活动发布管理")
@Validated
@RestController
@RequestMapping("/api/admin/activity/info")
public class ActivityInfoController {

  @Autowired private ActivityInfoService activityInfoService;

  @Autowired private ConfigService configService;

  @Operation(summary = "活动列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('activity:info:view')")
  public IPage<ActivityInfoVO> list(
      @Parameter(hidden = true) PageDTO<ActivityInfo> page, ActivityInfoQueryDTO dto) {
    return activityInfoService.list(page, dto);
  }

  @Operation(summary = "活动详情")
  @GetMapping("/detail")
  @PreAuthorize("hasAuthority('activity:info:detail')")
  public ActivityInfoVO detail(Long id) {
    if (id == null || id == 0) {
      throw new ServiceException("活动id不能为空");
    }
    return activityInfoService.detail(id);
  }

  @Operation(summary = "新增活动")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('activity:info:add')")
  public void add(@RequestBody ActivityInfoAddDTO activityInfoAddDTO) {
    checkActivityInfo(
        activityInfoAddDTO.getValidStatus(),
        activityInfoAddDTO.getEndTime(),
        activityInfoAddDTO.getBeginTime(),
        activityInfoAddDTO.getActivityLobbyId(),
        activityInfoAddDTO.getId());
    activityInfoAddDTO.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
    activityInfoService.add(activityInfoAddDTO);
  }

  private void checkActivityInfo(
      Integer validStatus, String endTime, String beginTime, Long activityLobbyId, Long id) {
    if (validStatus == ActivityInfoEnum.ValidStatus.TIME_LIMIT.value()) {
      if (DateUtil.strToDate(endTime, "yyyy-MM-dd")
          .before(DateUtil.strToDate(beginTime, "yyyy-MM-dd"))) {
        throw new ServiceException("活动结束时间不能小于活动开始时间");
      }
    }
    if (StringUtils.isNotNull(activityLobbyId) && StringUtils.isNull(id)) {
      activityInfoService.checkActivityLobbyId(activityLobbyId, id);
    }
  }

  @Operation(summary = "编辑活动")
  @PostMapping("/update")
  @PreAuthorize("hasAuthority('activity:info:edit')")
  public void update(@RequestBody ActivityInfoUpdateDTO activityInfoUpdateDTO) {
    checkActivityInfo(
        activityInfoUpdateDTO.getValidStatus(),
        activityInfoUpdateDTO.getEndTime(),
        activityInfoUpdateDTO.getBeginTime(),
        activityInfoUpdateDTO.getActivityLobbyId(),
        activityInfoUpdateDTO.getId());
    activityInfoUpdateDTO.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
    activityInfoService.update(activityInfoUpdateDTO);
  }

  @Operation(summary = "修改活动排序")
  @PostMapping("/updateSort")
  @PreAuthorize("hasAuthority('activity:info:updateSort')")
  public void updateSort(@Validated @RequestBody ActivityInfoUpdateSortDTO dto) {
    activityInfoService.updateSort(dto);
  }

  @Operation(summary = "删除活动")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('activity:info:remove')")
  public void delete(@RequestBody Map<String, String> map) {
    Assert.notNull(map.get("ids"), "id不能为空！");
    activityInfoService.delete(map.get("ids"));
  }

  @Operation(summary = "获取全部活动")
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

  @Operation(summary = "活动排序值列表")
  @GetMapping("/sortList")
  @PreAuthorize("hasAuthority('activity:info:sortList')")
  public List<ValueDataVO> sortList() {
    String activitySortConfig = configService.getValue(DictDataEnum.ACTIVITY_SORT_CONFIG);
    if (StringUtils.isBlank(activitySortConfig)) {
      throw new ServiceException("活动排序值没有配置");
    }
    return JSONArray.parseArray(activitySortConfig, ValueDataVO.class);
  }
}
