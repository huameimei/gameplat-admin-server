package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SpreadUnion;
import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.vo.SpreadUnionVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpreadUnionConvert {
    // 联运
    SpreadUnionVO toSpreadUnionVO(SpreadUnion spreadUnion);

    SpreadUnion toSpreadUnionDTO(SpreadUnionDTO spreadUnion);

}
