package com.gameplat.admin.convert;


import com.gameplat.admin.model.domain.MemberGrowthBanner;
import com.gameplat.admin.model.dto.MemberGrowthBannerAddDTO;
import com.gameplat.admin.model.dto.MemberGrowthBannerEditDTO;
import com.gameplat.admin.model.vo.MemberGrowthBannerVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberGrowthBannerConvert {

    MemberGrowthBanner toEntity(MemberGrowthBannerAddDTO dto);

    MemberGrowthBanner toEntity(MemberGrowthBannerEditDTO dto);

    MemberGrowthBannerVO toVo(MemberGrowthBanner dto);
}
