package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysDictTypeAddDto;
import com.gameplat.admin.model.dto.SysDictTypeEditDto;
import com.gameplat.admin.model.entity.SysDictType;
import com.gameplat.admin.model.vo.SysDictTypeVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysDictTypeConvert {

  SysDictType toEntity(SysDictTypeAddDto sysDictTypeAddDto);

  SysDictTypeVo toVo(SysDictType sysDictType);

  SysDictType toEntity(SysDictTypeEditDto sysDictTypeEditDto);
}
