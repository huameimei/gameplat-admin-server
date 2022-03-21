package com.gameplat.admin.controller.open.operator;

import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.SpreadTypes;
import com.gameplat.admin.model.dto.SpreadLinkInfoAddDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import com.gameplat.admin.service.SpreadLinkInfoService;
import com.gameplat.model.entity.spread.SpreadLinkInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 代理推广管理
 *
 * @author three
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/operator/diffusion/spreadConfig")
public class OpenSpreadLinkInfoController {

  @Autowired private SpreadLinkInfoService spreadLinkInfoService;

  @GetMapping("/list")
  //  @PreAuthorize("hasAuthority('diffusion:spreadConfig:view')")
  public IPage<SpreadConfigVO> list(PageDTO<SpreadLinkInfo> page, SpreadLinkInfoDTO configDTO) {
    return spreadLinkInfoService.page(page, configDTO);
  }

  @GetMapping("/exportList")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:view')")
  public void exportList(SpreadLinkInfoDTO configDTO, HttpServletResponse response) {
    spreadLinkInfoService.exportList(configDTO, response);
  }

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:add')")
  public void add(@RequestBody SpreadLinkInfoAddDTO configAddDTO) {
    spreadLinkInfoService.add(configAddDTO);
  }

  @PutMapping("/edit")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:edit')")
  public void edit(@RequestBody SpreadLinkInfoEditDTO configEditDTO) {
    spreadLinkInfoService.update(configEditDTO);
  }

  @DeleteMapping("/delete/{id}")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:remove')")
  public void remove(@PathVariable Long id) {
    spreadLinkInfoService.deleteById(id);
  }

  @DeleteMapping("/batchDelete")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchRemove')")
  public void batchRemove(@RequestBody List<Long> ids) {
    spreadLinkInfoService.batchDeleteByIds(ids);
  }

  @PostMapping("/changeStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:changeStatus')")
  public void changeStatus(@RequestBody SpreadLinkInfoEditDTO configEditDTO) {
    spreadLinkInfoService.changeStatus(configEditDTO);
  }

  @PostMapping("/changeReleaseTime/{id}")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:addEffectiveDay')")
  public void changeReleaseTime(@PathVariable Long id) {
    spreadLinkInfoService.changeReleaseTime(id);
  }

  @PutMapping("/batchEnableStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchEnableStatus')")
  public void batchEnableStatus(@RequestBody List<Long> ids) {
    spreadLinkInfoService.batchEnableStatus(ids);
  }

  @PutMapping("/batchDisableStatus")
  @PreAuthorize("hasAuthority('diffusion:spreadConfig:batchDisableStatus')")
  public void batchDisableStatus(@RequestBody List<Long> ids) {
    spreadLinkInfoService.batchDisableStatus(ids);
  }

  @GetMapping("/spreadTypeList")
  public List<Map<String, Object>> getSpreadTypesList() {
    return SpreadTypes.getAllList();
  }

  @GetMapping("/getAgentDomain")
  public List<String> getAgentDomain(@RequestParam String agentAccount) {
    List<SpreadLinkInfo> spreadList = spreadLinkInfoService.getSpreadList(agentAccount);
    List<String> listStr = new ArrayList<>();
    spreadList.forEach(x -> listStr.add(x.getExternalUrl()));
    return listStr;
  }

  /**
   * 添加推广码链接时 获取返点等级下拉框可选数据
   *
   * @return JSONArray
   */
  @GetMapping("/getLinkRebateOptionsForAdd")
  public JSONArray getLinkRebateOptionsForAdd(@RequestParam(required = false) String agentAccount) {
    return spreadLinkInfoService.getSpreadLinkRebate(agentAccount, true, false);
  }
  /**
   * 编辑推广码链接时 获取返点等级下拉框可选数据
   *
   * @return JSONArray
   */
  @GetMapping("/getLinkRebateOptionsForEdit")
  public JSONArray getLinkRebateOptionsForEdit(
      @RequestParam(required = false) String agentAccount) {
    return spreadLinkInfoService.getSpreadLinkRebate(agentAccount, true, false);
  }
}
