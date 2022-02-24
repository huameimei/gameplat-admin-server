package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.ValidWithdraw;
import com.gameplat.admin.model.dto.ValidWithdrawDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ValidWithdrawConvert {

    ValidWithdraw toEntity(ValidWithdrawDto dto);
}
