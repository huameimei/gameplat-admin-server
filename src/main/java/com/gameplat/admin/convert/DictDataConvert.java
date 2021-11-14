package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.dto.SysDictDataDTO;
import com.gameplat.admin.model.vo.DictDataVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictDataConvert {

  SysDictData toEntity(SysDictDataDTO dictDataDTO);

  SysDictData toEntity(OperDictDataDTO dictDataDTO);

  DictDataVo toVo(SysDictData dictData);
}