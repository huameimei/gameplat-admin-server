package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameRebateData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GameRebateDataMapper extends BaseMapper<GameRebateData> {

  List<GameRebateData> queryBetRecordList(GameRebateDataQueryDTO dto);

  List<GameReportVO> queryGameReport(GameRebateDataQueryDTO dto);
}
