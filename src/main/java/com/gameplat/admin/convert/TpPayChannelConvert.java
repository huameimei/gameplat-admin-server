package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.TpPayChannel;
import com.gameplat.admin.model.dto.TpPayChannelAddDTO;
import com.gameplat.admin.model.dto.TpPayChannelEditDTO;
import com.gameplat.admin.model.dto.TpPayChannelQueryDTO;
import com.gameplat.admin.model.vo.TpPayChannelVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TpPayChannelConvert {

    TpPayChannelVO toVo(TpPayChannel entity);

    TpPayChannel toEntity(TpPayChannelAddDTO payTypeAddDTO);

    TpPayChannel toEntity(TpPayChannelQueryDTO payTypeQueryDTO);

    TpPayChannel toEntity(TpPayChannelEditDTO payTypeEditDTO);
}
