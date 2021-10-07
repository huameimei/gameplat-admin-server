package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.SysDictTypeAddDTO;
import com.gameplat.admin.model.dto.SysDictTypeEditDTO;
import com.gameplat.admin.model.dto.SysDictTypeQueryDTO;
import com.gameplat.admin.model.entity.SysDictType;
import com.gameplat.admin.model.vo.SysDictTypeVO;

public interface SysDictTypeService extends IService<SysDictType> {

    void delete(Long id);

    void save(SysDictTypeAddDTO sysDictTypeAddDto);

    IPage<SysDictTypeVO> queryPage(IPage<SysDictType> page, SysDictTypeQueryDTO queryDto);

    void update(SysDictTypeEditDTO sysDictTypeEditDto);
}
