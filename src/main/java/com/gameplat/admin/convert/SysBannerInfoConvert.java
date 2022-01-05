package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.SysBannerInfo;
import com.gameplat.admin.model.dto.SysBannerInfoAddDTO;
import com.gameplat.admin.model.dto.SysBannerInfoEditDTO;
import com.gameplat.admin.model.vo.SysBannerInfoVO;
import org.mapstruct.Mapper;

/**
 * banner转换器
 *
 * @author admin
 */
@Mapper(componentModel = "spring")
public interface SysBannerInfoConvert {

    SysBannerInfoVO toVo(SysBannerInfo sysBannerInfo);

    SysBannerInfo toEntity(SysBannerInfoAddDTO sysBannerInfoAddDTO);

    SysBannerInfo toEntity(SysBannerInfoEditDTO sysBannerInfoEditDTO);
}
