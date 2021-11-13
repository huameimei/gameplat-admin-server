package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.dto.OperSysSmsAreaDTO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysSmsAreaConvert {
    SysSmsArea toEntity(OperSysSmsAreaDTO dto);

    SysSmsAreaVO toVo(SysSmsArea sysSmsArea);
}
