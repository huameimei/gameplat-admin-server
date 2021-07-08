package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysBannerInfoAddDTO;
import com.gameplat.admin.model.dto.SysBannerInfoEditDTO;
import com.gameplat.admin.model.entity.SysBannerInfo;
import com.gameplat.admin.model.vo.SysBannerInfoVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysBannerInfoConvert {

  SysBannerInfo toEntity(SysBannerInfoAddDTO sysBannerInfoAddDto);

  SysBannerInfo toEntity(SysBannerInfoEditDTO sysBannerInfoEditDto);

  SysBannerInfoVO toVo(SysBannerInfo sysBannerInfo);
}
