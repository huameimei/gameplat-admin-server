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
  // 联运
  SpreadUnionVO toSpreadUnionVO(SpreadUnion entity);

  SpreadUnion toSpreadUnionDTO(SpreadUnionDTO dto);

  // 联运
  SpreadUnionPackageVO toSpreadUnionPackageVO(SpreadUnion entity);

  // 联运包设置 SpreadUnionPackage
  SpreadUnionPackage toSpreadUnionPackageDTO(SpreadUnionPackageDTO dto);
}
