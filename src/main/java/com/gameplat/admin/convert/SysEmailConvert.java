package com.gameplat.admin.convert;

import com.gameplat.admin.model.entity.SysEmail;
import com.gameplat.admin.model.vo.SysEmailVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysEmailConvert {

  SysEmailVO toVo(SysEmail sysEmail);
}
