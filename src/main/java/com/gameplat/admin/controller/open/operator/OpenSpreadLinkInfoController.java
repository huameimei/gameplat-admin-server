package com.gameplat.admin.controller.open.operator;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoAddDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import com.gameplat.admin.service.SpreadLinkInfoService;
import com.gameplat.common.enums.SpreadTypes;
import com.gameplat.model.entity.spread.SpreadLinkInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "代理推广管理")
@Slf4j
@RestController
@RequestMapping("/api/admin/operator/diffusion/spreadConfig")
public class OpenSpreadLinkInfoController {

  @Autowired private SpreadLinkInfoService spreadLinkInfoService;

  @ApiOperation("查询")
  @GetMapping("/list")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:view')")
  public IPage<SpreadConfigVO> list(PageDTO<SpreadLinkInfo> page, SpreadLinkInfoDTO configDTO) {
    return spreadLinkInfoService.page(page, configDTO);
  }

  @ApiOperation("导出")
  @GetMapping("/exportList")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:export')")
  public void exportList(SpreadLinkInfoDTO configDTO, HttpServletResponse response) {
    spreadLinkInfoService.exportList(configDTO, response);
  }

  @ApiOperation("添加")
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:add')")
  public void add(@RequestBody SpreadLinkInfoAddDTO configAddDTO) {
    spreadLinkInfoService.add(configAddDTO);
  }

  @ApiOperation("编辑")
  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:edit')")
  public void edit(@RequestBody SpreadLinkInfoEditDTO configEditDTO) {
    spreadLinkInfoService.update(configEditDTO);
  }

  @ApiOperation("删除")
  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:remove')")
  public void remove(@PathVariable Long id) {
    spreadLinkInfoService.deleteById(id);
  }

  @ApiOperation("批量删除")
  @DeleteMapping("/batchDelete")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchRemove')")
  public void batchRemove(@RequestBody List<Long> ids) {
    spreadLinkInfoService.batchDeleteByIds(ids);
  }

  @ApiOperation("改变状态")
  @PostMapping("/changeStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:changeStatus')")
  public void changeStatus(@RequestBody SpreadLinkInfoEditDTO configEditDTO) {
    spreadLinkInfoService.changeStatus(configEditDTO);
  }

  @ApiOperation("更新发布时间")
  @PostMapping("/changeReleaseTime/{id}")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:addEffectiveDay')")
  public void changeReleaseTime(@PathVariable Long id) {
    spreadLinkInfoService.changeReleaseTime(id);
  }

  @ApiOperation("批量启用")
  @PutMapping("/batchEnableStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchEnableStatus')")
  public void batchEnableStatus(@RequestBody List<Long> ids) {
    spreadLinkInfoService.batchEnableStatus(ids);
  }

  @ApiOperation("批量禁用")
  @PutMapping("/batchDisableStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchDisableStatus')")
  public void batchDisableStatus(@RequestBody List<Long> ids) {
    spreadLinkInfoService.batchDisableStatus(ids);
  }

  @ApiOperation("推广类型列表")
  @GetMapping("/spreadTypeList")
  public List<Map<String, Object>> getSpreadTypesList() {
    return SpreadTypes.getAllList();
  }

  @ApiOperation("获取代理域名")
  @GetMapping("/getAgentDomain")
  public List<String> getAgentDomain(@RequestParam String agentAccount) {
    return spreadLinkInfoService.getSpreadList(agentAccount).stream()
        .map(SpreadLinkInfo::getExternalUrl)
        .collect(Collectors.toList());
  }

  @ApiOperation("添加推广码链接时，获取返点等级下拉框可选数据")
  @GetMapping("/getLinkRebateOptionsForAdd")
  public JSONArray getLinkRebateOptionsForAdd(@RequestParam(required = false) String agentAccount) {
    return spreadLinkInfoService.getSpreadLinkRebate(agentAccount, true, false);
  }

  @ApiOperation("编辑推广码链接时 获取返点等级下拉框可选数据")
  @GetMapping("/getLinkRebateOptionsForEdit")
  public JSONArray getLinkRebateOptionsForEdit(
      @RequestParam(required = false) String agentAccount) {
    return spreadLinkInfoService.getSpreadLinkRebate(agentAccount, true, false);
  }

  @ApiOperation("获取默认推广链接")
  @GetMapping("/getDefaultLink")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:add')")
  public List<SpreadConfigVO> getDefaultLink() {
    return spreadLinkInfoService.getDefaultLink();
  }
}
