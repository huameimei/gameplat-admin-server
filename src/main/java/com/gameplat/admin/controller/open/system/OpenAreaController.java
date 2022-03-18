package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.dto.SmsAreaAddDTO;
import com.gameplat.admin.model.dto.SmsAreaEditDTO;
import com.gameplat.admin.model.dto.SmsAreaQueryDTO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.service.SysSmsAreaService;
import com.gameplat.model.entity.sys.SysSmsArea;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/** 区号设置控制器 */
@Slf4j
@Api(tags = "区号设置API")
@RestController
@RequestMapping("/api/admin/system/smsArea")
public class OpenAreaController {

  @Autowired private SysSmsAreaService areaService;

  @ApiOperation("列表查询")
  @GetMapping("/list")
  public IPage<SysSmsAreaVO> findSmsAreaList(
      @ApiIgnore PageDTO<SysSmsArea> page, SmsAreaQueryDTO queryDTO) {
    return areaService.findSmsAreaList(page, queryDTO);
  }

  @ApiOperation("新增")
  @PostMapping("/add")
  public void saveArea(@Validated @RequestBody SmsAreaAddDTO addDTO) {
    areaService.addSmsArea(addDTO);
  }

  @ApiOperation("编辑")
  @PutMapping("/edit")
  public void updateArea(@Validated @RequestBody SmsAreaEditDTO editDTO) {
    areaService.editSmsArea(editDTO);
  }

  @ApiOperation("删除")
  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable Long id) {
    areaService.deleteAreaById(id);
  }

  @ApiOperation("改变状态")
  @PutMapping("/changeStatus/{id}/{status}")
  public void changeStatus(@PathVariable Long id, @PathVariable Integer status) {
    areaService.changeStatus(id, status);
  }


  @ApiOperation("设置默认区号")
  @PutMapping("/setDefaultStatus/{id}/{status}")
  public void setDefaultStatus(@PathVariable Long id, @PathVariable Integer status) {
    areaService.setDefaultStatus(id, status);
  }
}
