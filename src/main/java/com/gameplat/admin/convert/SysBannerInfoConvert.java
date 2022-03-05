package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.SysBannerInfoAddDTO;
import com.gameplat.admin.model.dto.SysBannerInfoEditDTO;
import com.gameplat.admin.model.vo.SysBannerInfoVO;
import com.gameplat.model.entity.sys.SysBannerInfo;
import org.mapstruct.Mapper;

/**
 * banner转换器
 *
 * @author admin
 */
@Mapper(componentModel = "spring")
public interface SysBannerInfoConvert {

  SysBannerInfoVO toVo(SysBannerInfo entity);

  SysBannerInfo toEntity(SysBannerInfoAddDTO dto);

  SysBannerInfo toEntity(SysBannerInfoEditDTO dto);
}
