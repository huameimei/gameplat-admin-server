package com.gameplat.admin.controller.open.operator;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.SpreadTypes;
import com.gameplat.admin.model.domain.SpreadConfig;
import com.gameplat.admin.model.dto.SpreadConfigAddDTO;
import com.gameplat.admin.model.dto.SpreadConfigDTO;
import com.gameplat.admin.model.dto.SpreadConfigEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import com.gameplat.admin.service.SpreadConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代理推广管理
 *
 * @author three
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/operator/diffusion/spreadConfig")
public class OpenSpreadConfigController {

  @Autowired private SpreadConfigService configService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:view')")
  public IPage<SpreadConfigVO> list(PageDTO<SpreadConfig> page, SpreadConfigDTO configDTO) {
    return configService.selectSpreadConfigList(page, configDTO);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:add')")
  public void add(@RequestBody SpreadConfigAddDTO configAddDTO) {
    configService.insertSpreadConfig(configAddDTO);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:edit')")
  public void edit(@RequestBody SpreadConfigEditDTO configEditDTO) {
    configService.updateSpreadConfig(configEditDTO);
  }

  @DeleteMapping("/delete")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:remove')")
  public void remove(@RequestBody String id) {
    configService.deleteSpreadConfig(id);
  }

  @DeleteMapping("/batchDelete")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchRemove')")
  public void batchRemove(@RequestBody String ids) {
    configService.batchDeleteSpreadConfig(ids);
  }

  @PostMapping("/changeStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:changeStatus')")
  public void changeStatus(@RequestBody SpreadConfigEditDTO configEditDTO) {
    configService.changeStatus(configEditDTO);
  }

  @PostMapping("/changeReleaseTime/{id}")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:addEffectiveDay')")
  public void changeReleaseTime(@PathVariable Long id) {
    configService.changeReleaseTime(id);
  }

  @GetMapping("/batchEnableStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchEnableStatus')")
  public void batchEnableStatus(String ids) {
    configService.batchEnableStatus(ids);
  }

  @GetMapping("/batchDisableStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchDisableStatus')")
  public void batchDisableStatus(String ids) {
    configService.batchDisableStatus(ids);
  }

  @GetMapping("/spreadTypeList")
  public List<JSONObject> getSpreadTypesList() {
    return SpreadTypes.getAllList();
  }
}
