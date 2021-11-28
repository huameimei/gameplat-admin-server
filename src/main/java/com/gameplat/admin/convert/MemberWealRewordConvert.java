package com.gameplat.admin.convert;

import com.gameplat.admin.model.domain.MemberWealReword;
import com.gameplat.admin.model.dto.MemberWealRewordAddDTO;
import com.gameplat.admin.model.vo.MemberWealRewordVO;
import org.mapstruct.Mapper;

/**
 * @author Lily
 */
@Mapper(componentModel = "spring")
public interface MemberWealRewordConvert {

    MemberWealRewordVO toVo(MemberWealReword reword);

    MemberWealReword toEntity (MemberWealRewordAddDTO dto);

}
