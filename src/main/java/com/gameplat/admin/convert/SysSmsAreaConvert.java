package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysSmsArea;
import com.gameplat.admin.model.dto.SmsAreaAddDTO;
import com.gameplat.admin.model.dto.SmsAreaEditDTO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysSmsAreaConvert {

    SysSmsArea toEntity(SmsAreaAddDTO addDTO);
    SysSmsArea toEntity(SmsAreaEditDTO editDTO);
    SysSmsAreaVO toVo(SysSmsArea sysSmsArea);
}
