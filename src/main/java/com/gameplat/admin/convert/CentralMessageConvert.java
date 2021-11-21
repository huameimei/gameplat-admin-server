package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.CentralMessage;
import com.gameplat.admin.model.vo.CentralMessageVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CentralMessageConvert {

    CentralMessageVO toVo(CentralMessage centralMessage);
}
