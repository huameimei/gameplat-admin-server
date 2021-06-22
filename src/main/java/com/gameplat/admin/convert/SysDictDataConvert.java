package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysDictDataAddDto;
import com.gameplat.admin.model.dto.SysDictDataEditDto;
import com.gameplat.admin.model.entity.SysDictData;
import com.gameplat.admin.model.vo.SysDictDataVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysDictDataConvert {

  SysDictData toEntity(SysDictDataAddDto sysDictDataAddDto);

  SysDictDataVo toVo(SysDictData sysDictData);

  SysDictData toEntity(SysDictDataEditDto sysDictDataEditDto);
}
