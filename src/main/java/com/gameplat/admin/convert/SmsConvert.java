package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysSMS;
import com.gameplat.admin.model.dto.OperSmsDTO;
import com.gameplat.admin.model.dto.SmsDTO;
import com.gameplat.admin.model.vo.SMSVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SmsConvert {

  SysSMS toEntity(SmsDTO smsDTO);

  SysSMS toEntity(OperSmsDTO smsDTO);

  SMSVO toVo(SysSMS sms);
}
