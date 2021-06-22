package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysBannerInfoAddDto;
import com.gameplat.admin.model.entity.SysBannerInfo;
import com.gameplat.admin.model.vo.SysBannerInfoVo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SysBannerInfoConvert {

  SysBannerInfo toEntity(SysBannerInfoAddDto sysBannerInfoAddDto);

  SysBannerInfoVo toVo(SysBannerInfo sysBannerInfo);

}
