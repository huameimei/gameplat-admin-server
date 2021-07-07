package com.gameplat.admin.convert;

import com.gameplat.admin.model.entity.SysSms;
import com.gameplat.admin.model.vo.SysSmsVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysSmsConvert {

  SysSmsVO toVo(SysSms sysSms);
}
