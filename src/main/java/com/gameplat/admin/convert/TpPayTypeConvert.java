package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.TpPayType;
import com.gameplat.admin.model.vo.TpPayTypeVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TpPayTypeConvert {

    TpPayTypeVO toVo(TpPayType entity);
}
