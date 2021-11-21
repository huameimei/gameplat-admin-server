package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberGrowthConfig;
import com.gameplat.admin.model.domain.MemberGrowthLevel;
import com.gameplat.admin.model.vo.MemberGrowthConfigVO;
import com.gameplat.admin.model.vo.MemberGrowthLevelVO;
import org.mapstruct.Mapper;

/**
 * @author lily
 * @description
 * @date 2021/11/20
 */

@Mapper(componentModel = "spring")
public interface MemberGrowthConfigConvert {
    MemberGrowthConfigVO toVo(MemberGrowthConfig config);
}
