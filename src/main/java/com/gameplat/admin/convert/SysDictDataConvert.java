package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysDictDataAddDTO;
import com.gameplat.admin.model.dto.SysDictDataEditDTO;
import com.gameplat.admin.model.entity.SysDictData;
import com.gameplat.admin.model.vo.SysDictDataVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysDictDataConvert {

  SysDictData toEntity(SysDictDataAddDTO sysDictDataAddDto);

  SysDictDataVO toVo(SysDictData sysDictData);

  SysDictData toEntity(SysDictDataEditDTO sysDictDataEditDto);
}
