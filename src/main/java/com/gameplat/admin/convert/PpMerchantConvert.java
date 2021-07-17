package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.PpMerchantAddDTO;
import com.gameplat.admin.model.dto.PpMerchantEditDTO;
import com.gameplat.admin.model.entity.PpMerchant;
import com.gameplat.admin.model.vo.PpMerchantVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PpMerchantConvert {

  PpMerchantVO toVo(PpMerchant entity);

  PpMerchant toEntity(PpMerchantAddDTO ppInterfaceAddDTO);

  PpMerchant toEntity(PpMerchantEditDTO ppMerchaneEditDTO);
}
