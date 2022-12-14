package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperDictDataDTO;
import com.gameplat.admin.model.dto.SysDictDataDTO;
import com.gameplat.admin.model.vo.DictDataVo;
import com.gameplat.model.entity.sys.SysDictData;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictDataConvert {

  SysDictData toEntity(SysDictDataDTO dto);

  SysDictData toEntity(OperDictDataDTO dto);

  DictDataVo toVo(SysDictData dictData);
}
