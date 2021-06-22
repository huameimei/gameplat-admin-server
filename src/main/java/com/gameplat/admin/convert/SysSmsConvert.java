package com.gameplat.admin.convert;

import com.gameplat.admin.model.entity.SysSms;
import com.gameplat.admin.model.vo.SysSmsVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysSmsConvert {

  SysSmsVo toVo(SysSms sysSms);

}
