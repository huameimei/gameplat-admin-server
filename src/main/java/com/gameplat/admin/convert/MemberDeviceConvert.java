package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MemberDeviceVO;
import com.gameplat.model.entity.member.MemberDevice;

/**
 * @author aBen
 * @date 2022/4/9 1:49
 * @desc
 */
public interface MemberDeviceConvert {

  MemberDeviceVO toVo(MemberDevice memberDevice);
}
