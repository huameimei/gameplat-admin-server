package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysDictTypeAddDTO;
import com.gameplat.admin.model.dto.SysDictTypeEditDTO;
import com.gameplat.admin.model.entity.SysDictType;
import com.gameplat.admin.model.vo.SysDictTypeVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysDictTypeConvert {

  SysDictType toEntity(SysDictTypeAddDTO sysDictTypeAddDto);

  SysDictTypeVO toVo(SysDictType sysDictType);

  SysDictType toEntity(SysDictTypeEditDTO sysDictTypeEditDto);
}
