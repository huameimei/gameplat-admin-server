package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.EmailDTO;
import com.gameplat.admin.model.dto.OperEmailDTO;
import com.gameplat.admin.model.vo.EmailVO;
import com.gameplat.model.entity.sys.SysEmail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailConvert {

  SysEmail toEntity(EmailDTO dto);

  SysEmail toEntity(OperEmailDTO dto);

  EmailVO toVo(SysEmail entity);
}
