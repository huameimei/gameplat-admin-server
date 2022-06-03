package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.RechargeOrderHistoryQueryDTO;
import com.gameplat.admin.model.vo.RechargeOrderHistoryVO;
import com.gameplat.admin.model.vo.RechargeOrderReportVo;
import com.gameplat.model.entity.recharge.RechargeOrderHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RechargeOrderHistoryConvert {

  RechargeOrderHistoryVO toVo(RechargeOrderHistory entity);

  RechargeOrderHistory toEntity(RechargeOrderHistoryQueryDTO dto);

  RechargeOrderReportVo toRechVo(RechargeOrderHistory entity);

}
