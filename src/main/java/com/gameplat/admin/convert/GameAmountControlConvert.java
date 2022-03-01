package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.GameAmountControl;
import com.gameplat.admin.model.vo.GameAmountControlVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GameAmountControlConvert {
  GameAmountControlVO toVo(GameAmountControl gameAmountControl);
}
