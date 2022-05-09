package com.gameplat.admin.controller.open.setting;

import com.alibaba.fastjson.JSON;
import com.gameplat.admin.model.vo.SysFloatTypeVo;
import com.gameplat.admin.service.SysFloatTypeService;
import com.gameplat.model.entity.setting.SysFloatSetting;
import com.gameplat.model.entity.setting.SysFloatType;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统消息
 *
 * @author key
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/open/float")
public class SysFloatTypeController {
  @Autowired private SysFloatTypeService sysFloatTypeService;

  /** 查询游戏浮窗类型列表 */
  @GetMapping("/query")
  @ApiOperation("游戏浮窗类型列表查询")
  public List<SysFloatTypeVo> list(SysFloatTypeVo sysFloatTypeVo) {
    SysFloatTypeVo sysFloatTypeVo1 = new SysFloatTypeVo();
    sysFloatTypeVo1.setOsType(sysFloatTypeVo.getOsType());
    return sysFloatTypeService.selectSysFloatTypeList(sysFloatTypeVo);
  }

  /** 新增游戏浮窗 */
  @PutMapping("/insert")
  @ApiOperation("游戏浮窗新增")
  @CacheEvict(cacheNames = "TENANT_FLOAT_LIST", allEntries = true)
  public void add(@RequestBody SysFloatSetting sysFloatSetting) {
    sysFloatTypeService.insertSysFloat(sysFloatSetting);
  }

  /** 编辑游戏浮窗 */
  // @PreAuthorize("@ss.hasPermi('kg:type:remove')")
  @PostMapping("/update")
  @ApiOperation("游戏浮窗编辑")
  @CacheEvict(cacheNames = "TENANT_FLOAT_LIST", allEntries = true)
  public void update(@RequestBody SysFloatSetting sysFloatSetting) {
    sysFloatTypeService.updateFloat(sysFloatSetting);
  }

  /** 批量编辑排序 */
  // @PreAuthorize("@ss.hasPermi('kg:type:remove')")
  @PostMapping("/updateBatch")
  @ApiOperation("批量编辑排序")
  @CacheEvict(cacheNames = "TENANT_FLOAT_LIST", allEntries = true)
  public void updateBatch(@RequestBody List<SysFloatSetting> sysFloatSettings) {
    sysFloatTypeService.updateBatch(sysFloatSettings);
  }

  /** 编辑游戏浮窗类型 */
  @PostMapping("/updateFloatType")
  @ApiOperation("游戏浮窗类型编辑")
  @CacheEvict(cacheNames = "TENANT_FLOAT_LIST", allEntries = true)
  public void updateFloatType(@RequestBody SysFloatType sysFloatType) {
    sysFloatTypeService.updateFloatType(sysFloatType);
  }

  /** 编辑游戏浮窗类型 */
  @PostMapping("/showPosition")
  @ApiOperation("游戏浮窗类型编辑")
  @CacheEvict(cacheNames = "TENANT_FLOAT_LIST", allEntries = true)
  public void showPosition(@RequestBody List<String> showPositionList) {
    sysFloatTypeService.updateShowPosition(JSON.toJSONString(showPositionList));
  }
}
