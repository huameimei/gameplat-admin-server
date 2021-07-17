package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.PayAccountAddDTO;
import com.gameplat.admin.model.dto.PayAccountEditDTO;
import com.gameplat.admin.model.dto.PayAccountQueryDTO;
import com.gameplat.admin.model.entity.PayAccount;
import com.gameplat.admin.model.vo.PayAccountVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PayAccountConvert {

  PayAccountVO toVo(PayAccount entity);

  PayAccount toEntity(PayAccountAddDTO payTypeAddDTO);

  PayAccount toEntity(PayAccountQueryDTO payTypeQueryDTO);

  PayAccount toEntity(PayAccountEditDTO payTypeEditDTO);
}
