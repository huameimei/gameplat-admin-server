package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.proxy.DivideDetail;
import com.gameplat.admin.model.vo.DivideDetailVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DivideDetailConvert {
    DivideDetailVO toVo(DivideDetail entity);
}
