package com.gameplat.admin.controller.open.operator;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.model.vo.SpreadUnionPackageVO;
import com.gameplat.admin.model.vo.SpreadUnionVO;
import com.gameplat.admin.service.SpreadUnionPackageService;
import com.gameplat.admin.service.SpreadUnionService;
import com.gameplat.model.entity.spread.SpreadUnion;
import com.gameplat.model.entity.spread.SpreadUnionPackage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "联盟管理")
@RestController
@RequestMapping("/api/admin/operator/management")
public class SpreadUnionController {

  @Autowired private SpreadUnionService spreadUnionService;

  @Autowired private SpreadUnionPackageService spreadUnionPackageService;

  @Operation(summary = "联盟增加")
  @PostMapping("/creatUnion")
  @PreAuthorize("hasAuthority('spreadUnion:union:add')")
  public void creatUnion(@RequestBody SpreadUnionDTO dto) {
    spreadUnionService.creatUnion(dto);
  }

  @Operation(summary = "联盟查询")
  @GetMapping("/unionList")
  @PreAuthorize("hasAuthority('spreadUnion:union:view')")
  public IPage<SpreadUnionVO> getUnion(
      @Parameter(hidden = true) PageDTO<SpreadUnion> page, SpreadUnionDTO dto) {
    return spreadUnionService.getUnion(page, dto);
  }

  @Operation(summary = "联盟修改")
  @PostMapping("/editUnion")
  @PreAuthorize("hasAuthority('spreadUnion:union:edit')")
  public void editUnion(@RequestBody SpreadUnionDTO dto) {
    spreadUnionService.editUnion(dto);
  }

  @Operation(summary = "联盟删除")
  @PostMapping("/removeUnion")
  @PreAuthorize("hasAuthority('spreadUnion:blacklist:remove')")
  public void removeUnion(@RequestBody List<Long> idList) {
    spreadUnionPackageService.removeByUnionId(idList);
    spreadUnionService.removeUnion(idList);
  }

  @Operation(summary = "联盟包设置列表")
  @GetMapping("/getUnionPackage")
  @PreAuthorize("hasAuthority('spreadUnion:unionpackage:view')")
  public List<SpreadUnionPackageVO> getUnionPackage(
      @Parameter(hidden = true) PageDTO<SpreadUnionPackage> page, SpreadUnionPackageDTO dto) {
    return spreadUnionPackageService.getUnionPackage(page, dto);
  }

  @Operation(summary = "联盟包设置增加")
  @PostMapping("/insertUnionPackage")
  @PreAuthorize("hasAuthority('spreadUnion:unionpackage:add')")
  public void insertUnionPackage(@RequestBody SpreadUnionPackageDTO dto) {
    spreadUnionPackageService.insertUnionPackage(dto);
  }

  @Operation(summary = "联盟包设置修改")
  @PostMapping("/editUnionPackage")
  @PreAuthorize("hasAuthority('spreadUnion:unionpackage:edit')")
  public void editUnionPackage(@RequestBody SpreadUnionPackageDTO dto) {
    spreadUnionPackageService.editUnionPackage(dto);
  }

  @Operation(summary = "联盟包设置删除")
  @PostMapping("/removeUnionPackage")
  @PreAuthorize("hasAuthority('spreadUnion:unionpackage:remove')")
  public void removeUnionPackage(@RequestBody List<Long> idList) {
    spreadUnionPackageService.removeUnionPackage(idList);
  }

  @Operation(summary = "联盟报表列表")
  @GetMapping("/unionReportList")
  @PreAuthorize("hasAuthority('spreadUnion:unionpackage:view')")
  public Object unionReportList(SpreadUnionDTO dto) {
    return spreadUnionService.getUnionReportList(dto);
  }

  @Operation(summary = "获取联盟报表详情")
  @GetMapping("/unionReportInfo")
  @PreAuthorize("hasAuthority('spreadUnion:unionpackage:info')")
  public List<JSONObject> unionReportInfo(
      @RequestParam("account") String account,
      @RequestParam("startTime") String startTime,
      @RequestParam("endTime") String endTime) {
    return spreadUnionService.unionReportInfo(account, startTime, endTime);
  }
}
