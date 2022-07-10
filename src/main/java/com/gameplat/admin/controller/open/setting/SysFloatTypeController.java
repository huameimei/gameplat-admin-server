package com.gameplat.admin.controller.open.setting;

import com.alibaba.fastjson.JSON;
import com.gameplat.admin.cache.AdminCache;
import com.gameplat.admin.model.vo.SysFloatTypeVo;
import com.gameplat.admin.service.SysFloatTypeService;
import com.gameplat.common.constant.CacheKey;
import com.gameplat.model.entity.setting.SysFloatSetting;
import com.gameplat.model.entity.setting.SysFloatType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统消息
 *
 * @author key
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/open/float")
public class SysFloatTypeController {

  @Autowired private AdminCache adminCache;

  @Autowired private SysFloatTypeService sysFloatTypeService;

  /** 查询游戏浮窗类型列表 */
  @GetMapping("/query")
  @Operation(summary = "游戏浮窗类型列表查询")
  public List<SysFloatTypeVo> list(SysFloatTypeVo sysFloatTypeVo) {
    SysFloatTypeVo sysFloatTypeVo1 = new SysFloatTypeVo();
    sysFloatTypeVo1.setOsType(sysFloatTypeVo.getOsType());
    return sysFloatTypeService.selectSysFloatTypeList(sysFloatTypeVo);
  }

  /** 新增游戏浮窗 */
  @PostMapping("/insert")
  @Operation(summary = "游戏浮窗新增")
  public void add(@RequestBody SysFloatSetting sysFloatSetting) {
    sysFloatTypeService.insertSysFloat(sysFloatSetting);
    adminCache.deleteByPrefix(CacheKey.getTenantFloatPrefixKey());
  }

  /** 编辑游戏浮窗 */
  @PostMapping("/update")
  @PreAuthorize("hasAuthority('setting:float:edit')")
  @Operation(summary = "游戏浮窗编辑")
  public void update(@RequestBody SysFloatSetting sysFloatSetting) {
    sysFloatTypeService.updateFloat(sysFloatSetting);
    adminCache.deleteByPrefix(CacheKey.getTenantFloatPrefixKey());
  }

  /** 批量编辑排序 */
  @PostMapping("/updateBatch")
  @PreAuthorize("hasAuthority('setting:floatsort:edit')")
  @Operation(summary = "批量编辑排序")
  public void updateBatch(@RequestBody List<SysFloatSetting> sysFloatSettings) {
    sysFloatTypeService.updateBatch(sysFloatSettings);
    adminCache.deleteByPrefix(CacheKey.getTenantFloatPrefixKey());
  }

  /** 编辑游戏浮窗类型 */
  @PostMapping("/updateFloatType")
  @PreAuthorize("hasAuthority('setting:floattype:edit')")
  @Operation(summary = "游戏浮窗类型编辑")
  public void updateFloatType(@RequestBody SysFloatType sysFloatType) {
    sysFloatTypeService.updateFloatType(sysFloatType);
    adminCache.deleteByPrefix(CacheKey.getTenantFloatPrefixKey());
  }

  /** 编辑游戏浮窗类型 */
  @PostMapping("/showPosition")
  @PreAuthorize("hasAuthority('setting:gamefloat:edit')")
  @Operation(summary = "游戏浮窗类型编辑")
  public void showPosition(@RequestBody Map<String, List<String>> showPosition) {
    if (showPosition != null && showPosition.get("midArr") != null) {
      sysFloatTypeService.updateShowPosition(JSON.toJSONString(showPosition.get("midArr")));
    }
  }
}
