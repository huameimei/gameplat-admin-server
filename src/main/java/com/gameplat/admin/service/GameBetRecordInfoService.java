package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.dto.UserGameBetRecordDto;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.common.game.GameResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public interface GameBetRecordInfoService {

  PageDtoVO<GameBetRecordVO> queryPageBetRecord(
      Page<GameBetRecordVO> page, GameBetRecordQueryDTO dto);

  GameResult getGameResult(GameBetRecordQueryDTO dto) throws Exception;

  List<ActivityStatisticItem> xjAssignMatchDml(Map map);

  void exportReport(GameBetRecordQueryDTO dto, HttpServletResponse response);

  void exportUserGameBetRecord(GameBetRecordQueryDTO dto, HttpServletResponse response) throws IOException;
}
