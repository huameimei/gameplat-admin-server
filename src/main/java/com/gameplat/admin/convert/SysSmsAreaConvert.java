package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SmsAreaAddDTO;
import com.gameplat.admin.model.dto.SmsAreaEditDTO;
import com.gameplat.admin.model.vo.SysSmsAreaVO;
import com.gameplat.model.entity.sys.SysSmsArea;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysSmsAreaConvert {

  SysSmsArea toEntity(SmsAreaAddDTO dto);

  SysSmsArea toEntity(SmsAreaEditDTO dto);

  SysSmsAreaVO toVo(SysSmsArea entity);
}
