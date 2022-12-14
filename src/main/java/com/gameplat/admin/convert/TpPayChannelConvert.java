package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.TpPayChannelAddDTO;
import com.gameplat.admin.model.dto.TpPayChannelEditDTO;
import com.gameplat.admin.model.dto.TpPayChannelQueryDTO;
import com.gameplat.admin.model.vo.TpPayChannelVO;
import com.gameplat.model.entity.pay.TpPayChannel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TpPayChannelConvert {

  TpPayChannelVO toVo(TpPayChannel entity);

  TpPayChannel toEntity(TpPayChannelAddDTO dto);

  TpPayChannel toEntity(TpPayChannelQueryDTO dto);

  TpPayChannel toEntity(TpPayChannelEditDTO dto);
}
