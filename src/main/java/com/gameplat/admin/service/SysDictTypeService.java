package com.gameplat.admin.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysDictTypeAddDto;
import com.gameplat.admin.model.dto.SysDictTypeEditDto;
import com.gameplat.admin.model.dto.SysDictTypeQueryDto;
import com.gameplat.admin.model.entity.SysDictType;
import com.gameplat.admin.model.vo.SysDictTypeVo;

public interface SysDictTypeService extends IService<SysDictType> {

  void delete(Long id);

  void save(SysDictTypeAddDto sysDictTypeAddDto);

  IPage<SysDictTypeVo> queryPage(IPage<SysDictType> page, SysDictTypeQueryDto queryDto);

  void update(SysDictTypeEditDto sysDictTypeEditDto);
}
