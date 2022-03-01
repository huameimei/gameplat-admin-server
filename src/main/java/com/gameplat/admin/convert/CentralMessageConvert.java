package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.CentralMessageVO;
import com.gameplat.model.entity.CentralMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CentralMessageConvert {

  CentralMessageVO toVo(CentralMessage entity);
}
