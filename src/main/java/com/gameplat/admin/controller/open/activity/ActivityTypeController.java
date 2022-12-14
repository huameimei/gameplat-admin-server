package com.gameplat.admin.controller.open.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.ActivityTypeAddDTO;
import com.gameplat.admin.model.dto.ActivityTypeQueryDTO;
import com.gameplat.admin.model.dto.ActivityTypeUpdateDTO;
import com.gameplat.admin.model.vo.ActivityTypeVO;
import com.gameplat.admin.model.vo.CodeDataVO;
import com.gameplat.admin.service.ActivityTypeService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.constant.ServiceName;
import com.gameplat.common.enums.BooleanEnum;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.log.annotation.Log;
import com.gameplat.log.enums.LogType;
import com.gameplat.model.entity.activity.ActivityType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 活动板块、管理
 *
 * @author kevin
 * @since 2020-08-17
 */
@Slf4j
@Tag(name = "活动板块管理")
@RestController
@RequestMapping("/api/admin/activity/type")
public class ActivityTypeController {

  @Autowired private ActivityTypeService activityTypeService;

  @Autowired private ConfigService configService;

  @Operation(summary = "活动板块列表")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('activity:type:view')")
  public IPage<ActivityTypeVO> list(
      @Parameter(hidden = true) PageDTO<ActivityType> page, ActivityTypeQueryDTO dto) {
    if (StringUtils.isBlank(dto.getLanguage())) {
      dto.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
    }
    if (!dto.getLanguage().contains("zh-CN,en-US,in-ID,th-TH,vi-VN")) {
      dto.setLanguage("zh-CN");
    }
    return activityTypeService.list(page, dto);
  }

  @Operation(summary = "新增活动板块")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('activity:type:add')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动板块管理->新增活动板块:' + #dto")
  public void add(@Validated @RequestBody ActivityTypeAddDTO dto) {
    dto.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
    if (!dto.getLanguage().contains("zh-CN,en-US,in-ID,th-TH,vi-VN")) {
      dto.setLanguage("zh-CN");
    }
    if (dto.getFloatStatus() != null && dto.getFloatStatus() != 0) {
      if (StringUtils.isBlank(dto.getFloatLogo())) {
        throw new ServiceException("开启浮窗开关，浮窗图片不能为空");
      }
    }
    if (StringUtils.isBlank(dto.getLanguage())) {
      throw new ServiceException("语言不能为空");
    }
    activityTypeService.add(dto);
  }

  @Operation(summary = "更新活动板块")
  @PostMapping("/update")
  @PreAuthorize("hasAuthority('activity:type:edit')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动板块管理->更新活动板块:' + #dto")
  public void update(@Validated @RequestBody ActivityTypeUpdateDTO dto) {
    dto.setLanguage(LocaleContextHolder.getLocale().toLanguageTag());
    if (!dto.getLanguage().contains("zh-CN,en-US,in-ID,th-TH,vi-VN")) {
      dto.setLanguage("zh-CN");
    }
    if (dto.getFloatStatus() != null && dto.getFloatStatus() != BooleanEnum.NO.value()) {
      if (StringUtils.isBlank(dto.getFloatLogo())) {
        throw new ServiceException("开启浮窗开关，浮窗图片不能为空");
      }
    }
    if (StringUtils.isBlank(dto.getLanguage())) {
      throw new ServiceException("语言language不能为空");
    }
    activityTypeService.update(dto);
  }

  @Operation(summary = "删除活动板块")
  @PostMapping("/delete")
  @PreAuthorize("hasAuthority('activity:type:remove')")
  @Log(
    module = ServiceName.ADMIN_SERVICE,
    type =  LogType.ADMIN,
    desc = "'活动板块管理->删除活动板块ids:' + #map.ids")
  public void remove(@RequestBody Map<String, String> map) {
    if (StringUtils.isBlank(map.get("ids"))) {
      throw new ServiceException("ids不能为空");
    }
    activityTypeService.remove(map.get("ids"));
  }

  @Operation(summary = "类型编码列表")
  @GetMapping("/typeCodeList")
  @PreAuthorize("hasAuthority('activity:type:typeCodeList')")
  public List<CodeDataVO> typeCodeList() {
    String activityTypeConfig = configService.getValue(DictDataEnum.ACTIVITY_TYPE_CONFIG);
    if (StringUtils.isEmpty(activityTypeConfig)) {
      throw new ServiceException("活动板块类型配置信息不存在");
    }

    String language = LocaleContextHolder.getLocale().toLanguageTag();
    JSONObject jsonObject = JSONObject.parseObject(activityTypeConfig);
    JSONArray jsonArray = jsonObject.getJSONArray(language);
    if (CollectionUtils.isEmpty(jsonArray)) {
      // 默认中文
      jsonArray = jsonObject.getJSONArray("zh-CN");
    }

    List<CodeDataVO> codeDataVOList = new ArrayList<>();
    for (int i = 0; i < jsonArray.size(); i++) {
      CodeDataVO codeDataVO = jsonArray.getObject(i, CodeDataVO.class);
      codeDataVOList.add(codeDataVO);
    }
    return codeDataVOList;
  }

  @Operation(summary = "活动板块查询所有列表")
  @GetMapping("/listAll")
  @PreAuthorize("hasAuthority('activity:type:listAll')")
  public List<ActivityTypeVO> listAll() {
    return activityTypeService.listAll();
  }
}
