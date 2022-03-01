package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.proxy.DivideSummary;
import com.gameplat.admin.model.dto.DivideSummaryDto;
import com.gameplat.admin.model.vo.DivideSummaryVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DivideSummaryConvert {
    DivideSummaryVO toVo(DivideSummary entity);

    DivideSummary toEntity(DivideSummaryDto divideSummaryDto);
}
