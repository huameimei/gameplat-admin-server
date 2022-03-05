package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.model.entity.game.GameBetRecord;
import org.mapstruct.Mapper;

/**
 * @author: asky
 * @date: 2022/1/14 19:48
 * @desc:
 */
@Mapper(componentModel = "spring")
public interface GameBetRecordConvert {

  /**
   * 转换为VO
   *
   * @param betRecord GameBetRecord
   * @return GameBetRecordVO
   */
  GameBetRecordVO toVo(GameBetRecord betRecord);
}
