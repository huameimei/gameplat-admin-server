package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.RechargeOrderQueryDTO;
import com.gameplat.admin.model.vo.RechargeOrderVO;
import com.gameplat.model.entity.recharge.RechargeOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RechargeOrderConvert {

  RechargeOrderVO toVo(RechargeOrder entity);

  RechargeOrder toEntity(RechargeOrderQueryDTO dto);
}
