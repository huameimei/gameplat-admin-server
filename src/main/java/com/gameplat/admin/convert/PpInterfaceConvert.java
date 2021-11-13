package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.PpInterface;
import com.gameplat.admin.model.vo.PpInterfaceVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PpInterfaceConvert {

  PpInterfaceVO toVo(PpInterface entity);
}
