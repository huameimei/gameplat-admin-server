package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.DivideDetailDto;
import com.gameplat.admin.model.vo.DivideDetailVO;
import com.gameplat.model.entity.proxy.DivideDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DivideDetailConvert {
  DivideDetailVO toVo(DivideDetail entity);

  DivideDetail toEntity(DivideDetailDto detailDto);
}
