package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.DividePeriodsDTO;
import com.gameplat.admin.model.vo.DividePeriodsVO;
import com.gameplat.model.entity.proxy.DividePeriods;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DividePeriodsConvert {
  DividePeriodsVO toVo(DividePeriods entity);

  DividePeriods toEntity(DividePeriodsDTO dto);
}
