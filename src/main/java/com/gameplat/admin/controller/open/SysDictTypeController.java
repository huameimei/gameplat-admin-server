package com.gameplat.admin.controller.open;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.SysDictTypeAddDTO;
import com.gameplat.admin.model.dto.SysDictTypeEditDTO;
import com.gameplat.admin.model.dto.SysDictTypeQueryDTO;
import com.gameplat.admin.model.entity.SysDictType;
import com.gameplat.admin.model.vo.SysDictTypeVO;
import com.gameplat.admin.service.SysDictTypeService;
import com.gameplat.common.constant.ServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ServiceApi.API + "/dictType")
public class SysDictTypeController {

  @Autowired private SysDictTypeService sysDictTypeService;

  @GetMapping(value = "/queryAll")
  public IPage<SysDictTypeVO> queryAll(Page<SysDictType> page, SysDictTypeQueryDTO queryDto) {
    return sysDictTypeService.queryPage(page, queryDto);
  }

  @PostMapping(value = "/save")
  public void save(SysDictTypeAddDTO sysDictTypeAddDto) {
    sysDictTypeService.save(sysDictTypeAddDto);
  }

  @PostMapping(value = "/update")
  public void update(SysDictTypeEditDTO sysDictTypeEditDto) {
    sysDictTypeService.update(sysDictTypeEditDto);
  }

  @DeleteMapping(value = "/delete/{id}")
  public void delete(@PathVariable("id") Long id) {
    sysDictTypeService.delete(id);
  }
}
