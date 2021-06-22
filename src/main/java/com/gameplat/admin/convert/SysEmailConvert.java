package com.gameplat.admin.convert;

import com.gameplat.admin.model.entity.SysEmail;
import com.gameplat.admin.model.entity.SysSms;
import com.gameplat.admin.model.vo.SysEmailVo;
import com.gameplat.admin.model.vo.SysSmsVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysEmailConvert {

  SysEmailVo toVo(SysEmail sysEmail);

}
