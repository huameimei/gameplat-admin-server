package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.PpInterfaceVO;
import com.gameplat.model.entity.pay.PpInterface;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PpInterfaceConvert {

  PpInterfaceVO toVo(PpInterface entity);
}
