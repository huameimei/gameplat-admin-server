package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ValidWithdrawDto;
import com.gameplat.admin.model.dto.ValidWithdrawOperateDto;
import com.gameplat.model.entity.ValidWithdraw;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ValidWithdrawConvert {

  ValidWithdraw toEntity(ValidWithdrawDto dto);

  @Mappings({
    @Mapping(source = "userId", target = "memberId"),
    @Mapping(source = "username", target = "account"),
    @Mapping(source = "remarks", target = "remark")
  })
  ValidWithdraw toValidWithdraw(ValidWithdrawOperateDto dto);
}
