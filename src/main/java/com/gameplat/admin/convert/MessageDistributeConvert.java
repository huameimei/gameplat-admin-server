package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.MessageDistributeVO;
import com.gameplat.model.entity.message.MessageDistribute;
import org.mapstruct.Mapper;

/**
 * 消息分发转换器
 *
 * @author admin
 */
@Mapper(componentModel = "spring")
public interface MessageDistributeConvert {

  MessageDistributeVO toVo(MessageDistribute messageDistribute);
}
