package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.DiscountTypeAddDTO;
import com.gameplat.admin.model.dto.DiscountTypeEditDTO;
import com.gameplat.admin.model.vo.DiscountTypeVO;
import com.gameplat.model.entity.DiscountType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiscountTypeConvert {

  DiscountType toEntity(DiscountTypeAddDTO dto);

  DiscountType toEntity(DiscountTypeEditDTO dto);

  DiscountTypeVO toVo(DiscountType entity);
}
