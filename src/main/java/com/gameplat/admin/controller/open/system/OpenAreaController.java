package com.gameplat.admin.controller.open.system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.dto.*;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.admin.service.SysSmsAreaService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * 区号设置控制器
 *
 * @author three
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "区号设置API")
@RequestMapping("/api/admin/system/smsArea")
public class OpenAreaController {

    @Autowired
    private SysSmsAreaService areaService;

    @GetMapping("/list")
    public IPage<SysSmsAreaVO> findSmsAreaList(PageDTO<SysSmsArea> page, SmsAreaQueryDTO queryDTO) {
        return areaService.findSmsAreaList(page, queryDTO);
    }

    @PostMapping("/add")
    public void saveArea(@RequestBody SmsAreaAddDTO addDTO) {
        areaService.addSmsArea(addDTO);
    }

    @PutMapping("/edit")
    public void updateArea(@RequestBody SmsAreaEditDTO editDTO) {
        areaService.editSmsArea(editDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        areaService.deleteAreaById(id);
    }

    @PutMapping("/changeStatus/{id}/{status}")
    public void changeStatus(@PathVariable Long id, @PathVariable Integer status) {
        areaService.changeStatus(id, status);
    }

}
