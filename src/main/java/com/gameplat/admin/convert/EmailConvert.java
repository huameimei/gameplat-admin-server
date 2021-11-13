package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysEmail;
import com.gameplat.admin.model.dto.EmailDTO;
import com.gameplat.admin.model.dto.OperEmailDTO;
import com.gameplat.admin.model.vo.EmailVO;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Repository;

@Mapper(componentModel = "spring")
public interface EmailConvert {

  SysEmail toEntity(EmailDTO emailDTO);

  SysEmail toEntity(OperEmailDTO emailDTO);

  EmailVO toVo(SysEmail email);
}
