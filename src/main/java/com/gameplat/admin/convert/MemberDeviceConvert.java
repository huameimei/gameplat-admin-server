package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberDeviceVO;
import com.gameplat.model.entity.member.MemberDevice;
import org.mapstruct.Mapper;

/**
 * @author aBen
 * @date 2022/4/9 1:49
 * @desc
 */
@Mapper(componentModel = "spring")
public interface MemberDeviceConvert {

  MemberDeviceVO toVo(MemberDevice memberDevice);
}
