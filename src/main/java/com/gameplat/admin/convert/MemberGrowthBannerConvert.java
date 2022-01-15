package com.gameplat.admin.convert;


import com.gameplat.admin.model.domain.MemberGrowthBanner;
import com.gameplat.admin.model.dto.MemberGrowthBannerDTO;
import com.gameplat.admin.model.vo.MemberGrowthBannerVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberGrowthBannerConvert {

    MemberGrowthBanner toEntity(MemberGrowthBannerDTO dto);

    MemberGrowthBannerVO toVo(MemberGrowthBanner dto);
}
