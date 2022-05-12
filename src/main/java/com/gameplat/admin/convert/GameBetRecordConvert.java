package com.gameplat.admin.convert;

import com.gameplat.admin.model.vo.GameBetRecordExportVO;
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

  GameBetRecordVO toVo(GameBetRecord betRecord);

  GameBetRecordExportVO toExportVo(GameBetRecord betRecord);
}
