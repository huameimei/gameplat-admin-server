package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SpreadUnionDTO;
import com.gameplat.admin.model.dto.SpreadUnionPackageDTO;
import com.gameplat.admin.model.vo.SpreadUnionPackageVO;
import com.gameplat.admin.model.vo.SpreadUnionVO;
import com.gameplat.model.entity.spread.SpreadUnion;
import com.gameplat.model.entity.spread.SpreadUnionPackage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpreadUnionConvert {
  SpreadUnionVO toSpreadUnionVO(SpreadUnion entity);

  SpreadUnion toSpreadUnionDTO(SpreadUnionDTO dto);

  SpreadUnionPackageVO toSpreadUnionPackageVO(SpreadUnion entity);

  SpreadUnionPackage toSpreadUnionPackageDTO(SpreadUnionPackageDTO dto);
}
