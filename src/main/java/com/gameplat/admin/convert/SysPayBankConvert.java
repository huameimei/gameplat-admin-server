package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysPayBankAddDTO;
import com.gameplat.admin.model.dto.SysPayBankEditDTO;
import com.gameplat.admin.model.entity.SysPayBank;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysPayBankConvert {

    SysPayBank toEntity(SysPayBankAddDTO payTypeAddDTO);

    SysPayBank toEntity(SysPayBankEditDTO configEditDTO);
}
