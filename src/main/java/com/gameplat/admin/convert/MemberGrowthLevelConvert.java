package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.GrowthLevelLogoEditDTO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import com.gameplat.model.entity.member.MemberGrowthLevel;
import org.mapstruct.Mapper;

/**
 * @author lily
 * @description
 * @date 2021/11/20
 */
@Mapper(componentModel = "spring")
public interface MemberGrowthLevelConvert {
  MemberGrowthLevelVO toVo(MemberGrowthLevel level);

  MemberGrowthLevel toEntity(GrowthLevelLogoEditDTO dto);
}
