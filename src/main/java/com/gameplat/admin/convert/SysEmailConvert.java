package com.gameplat.admin.convert;

import com.gameplat.admin.model.entity.SysEmail;
import com.gameplat.admin.model.vo.SysEmailVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysEmailConvert {

  SysEmailVo toVo(SysEmail sysEmail);

}
