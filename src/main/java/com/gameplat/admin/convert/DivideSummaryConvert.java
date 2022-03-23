package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.DivideSummaryDto;
import com.gameplat.admin.model.vo.DivideSummaryVO;
import com.gameplat.model.entity.proxy.DivideSummary;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DivideSummaryConvert {
  DivideSummaryVO toVo(DivideSummary entity);

  DivideSummary toEntity(DivideSummaryDto dto);
}
