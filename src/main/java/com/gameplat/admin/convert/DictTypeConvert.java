package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.OperDictTypeDTO;
import com.gameplat.admin.model.vo.DictTypeVO;
import com.gameplat.model.entity.sys.SysDictType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DictTypeConvert {

  DictTypeVO toVo(SysDictType entity);

  SysDictType toEntity(OperDictTypeDTO dto);
}
