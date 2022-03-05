package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.TpPayTypeVO;
import com.gameplat.model.entity.pay.TpPayType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TpPayTypeConvert {

  TpPayTypeVO toVo(TpPayType entity);
}
