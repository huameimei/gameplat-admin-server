package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SpreadLinkInfoAddDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoDTO;
import com.gameplat.admin.model.dto.SpreadLinkInfoEditDTO;
import com.gameplat.admin.model.vo.SpreadConfigVO;
import com.gameplat.model.entity.spread.SpreadLinkInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpreadLinkInfoConvert {

  SpreadLinkInfo toEntity(SpreadLinkInfoDTO configDTO);

  SpreadLinkInfo toEntity(SpreadLinkInfoAddDTO configAddDTO);

  SpreadLinkInfo toEntity(SpreadLinkInfoEditDTO configEditDTO);

  SpreadConfigVO toVo(SpreadLinkInfo config);
}
