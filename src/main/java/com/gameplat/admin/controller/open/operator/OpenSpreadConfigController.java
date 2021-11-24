package com.gameplat.admin.controller.open.operator;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.SpreadTypes;
import com.gameplat.admin.model.domain.SpreadLinkInfo;
import com.gameplat.admin.model.dto.SpreadLinkInfoAddDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import com.gameplat.admin.service.SpreadLinkInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

  @Autowired private SpreadLinkInfoService configService;

  @GetMapping("/list")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:view')")
  public IPage<SpreadConfigVO> list(PageDTO<SpreadLinkInfo> page, SpreadLinkInfoDTO configDTO) {
    return configService.page(page, configDTO);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:add')")
  public void add(@RequestBody SpreadLinkInfoAddDTO configAddDTO) {
    configService.add(configAddDTO);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:edit')")
  public void edit(@RequestBody SpreadLinkInfoEditDTO configEditDTO) {
    configService.update(configEditDTO);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:remove')")
  public void remove(@PathVariable Long id) {
    configService.deleteById(id);
  }

  @DeleteMapping("/batchDelete")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchRemove')")
  public void batchRemove(@RequestBody List<Long> ids) {
    configService.batchDeleteByIds(ids);
  }

  @PostMapping("/changeStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:changeStatus')")
  public void changeStatus(@RequestBody SpreadLinkInfoEditDTO configEditDTO) {
    configService.changeStatus(configEditDTO);
  }

  @PostMapping("/changeReleaseTime/{id}")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:addEffectiveDay')")
  public void changeReleaseTime(@PathVariable Long id) {
    configService.changeReleaseTime(id);
  }

  @GetMapping("/batchEnableStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchEnableStatus')")
  public void batchEnableStatus(@RequestBody List<Long> ids) {
    configService.batchEnableStatus(ids);
  }

  @GetMapping("/batchDisableStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchDisableStatus')")
  public void batchDisableStatus(@RequestBody List<Long> ids) {
    configService.batchDisableStatus(ids);
  }

  @GetMapping("/spreadTypeList")
  public List<Map<String, Object>> getSpreadTypesList() {
    return SpreadTypes.getAllList();
  }
}
