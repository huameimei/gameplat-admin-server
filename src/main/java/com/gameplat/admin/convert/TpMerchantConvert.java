package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.TpMerchantAddDTO;
import com.gameplat.admin.model.dto.TpMerchantEditDTO;
import com.gameplat.admin.model.vo.TpMerchantPayTypeVO;
import com.gameplat.admin.model.vo.TpMerchantVO;
import com.gameplat.model.entity.pay.TpMerchant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TpMerchantConvert {

  TpMerchantVO toVo(TpMerchant entity);

  TpMerchantPayTypeVO toPayTypeVo(TpMerchant entity);

  TpMerchant toEntity(TpMerchantAddDTO dto);

  TpMerchant toEntity(TpMerchantEditDTO dto);
}
