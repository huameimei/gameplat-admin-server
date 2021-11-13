package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.DiscountType;
import com.gameplat.admin.model.dto.DiscountTypeAddDTO;
import com.gameplat.admin.model.dto.DiscountTypeEditDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiscountTypeConvert {

  DiscountType toEntity(DiscountTypeAddDTO discountTypeAddDTO);

  DiscountType toEntity(DiscountTypeEditDTO discountTypeEditDTO);
}
