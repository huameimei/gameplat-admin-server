package com.gameplat.admin.convert;

import com.gameplat.admin.model.dto.ActivityRedPacketAddDTO;
import com.gameplat.admin.model.dto.ActivityRedPacketUpdateDTO;
import com.gameplat.admin.model.vo.ActivityRedPacketVO;
import com.gameplat.model.entity.activity.ActivityRedPacket;
import org.mapstruct.Mapper;

/**
 * 红包雨转换类
 *
 * @author admin
 */
@Mapper(componentModel = "spring")
public interface ActivityRedPacketConvert {

  ActivityRedPacket toEntity(ActivityRedPacketAddDTO activityRedPacketDTO);

  ActivityRedPacket toEntity(ActivityRedPacketUpdateDTO activityRedPacketUpdateDTO);

  /**
   * 将Entity转换成VO
   *
   * @param activityRedPacket
   * @return
   */
  ActivityRedPacketVO toVo(ActivityRedPacket activityRedPacket);
}
