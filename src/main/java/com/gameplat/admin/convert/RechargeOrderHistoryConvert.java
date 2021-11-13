package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.RechargeOrderHistory;
import com.gameplat.admin.model.dto.RechargeOrderHistoryQueryDTO;
import com.gameplat.admin.model.vo.RechargeOrderHistoryVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RechargeOrderHistoryConvert {

  RechargeOrderHistoryVO toVo(RechargeOrderHistory entity);

  RechargeOrderHistory toEntity(RechargeOrderHistoryQueryDTO dto);

}
