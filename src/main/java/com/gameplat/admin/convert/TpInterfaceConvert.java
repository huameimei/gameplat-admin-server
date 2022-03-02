package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.TpInterfacePayTypeVo;
import com.gameplat.admin.model.vo.TpInterfaceVO;
import com.gameplat.model.entity.pay.TpInterface;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TpInterfaceConvert {

  TpInterfaceVO toVo(TpInterface entity);

  TpInterfacePayTypeVo toTpInterfacePayTypeVo(TpInterface entity);
}
