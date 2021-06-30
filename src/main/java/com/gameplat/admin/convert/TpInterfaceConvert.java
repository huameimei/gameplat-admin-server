package com.gameplat.admin.convert;

import com.gameplat.admin.model.entity.TpInterface;
import com.gameplat.admin.model.vo.TpInterfaceVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TpInterfaceConvert {

  TpInterfaceVO toVo(TpInterface entity);
}
