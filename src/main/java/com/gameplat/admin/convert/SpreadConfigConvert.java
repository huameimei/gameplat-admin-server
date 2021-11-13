package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SpreadConfig;
import com.gameplat.admin.model.dto.SpreadConfigAddDTO;
import com.gameplat.admin.model.dto.SpreadConfigDTO;
import com.gameplat.admin.model.dto.SpreadConfigEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpreadConfigConvert {

  SpreadConfig toEntity(SpreadConfigDTO configDTO);

  SpreadConfig toEntity(SpreadConfigAddDTO configAddDTO);

  SpreadConfig toEntity(SpreadConfigEditDTO configEditDTO);

  SpreadConfigVO toVo(SpreadConfig config);
}
