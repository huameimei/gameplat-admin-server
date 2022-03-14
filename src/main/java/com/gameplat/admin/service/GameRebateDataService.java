package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.model.entity.game.GamePlatform;
import com.gameplat.model.entity.game.GameRebateData;

import java.util.List;

public interface GameRebateDataService extends IService<GameRebateData> {

  PageDtoVO<GameRebateData> queryPageData(Page<GameRebateData> page, GameRebateDataQueryDTO dto);

  void saveRebateDayReport(String statTime, GamePlatform gamePlatform);

  List<GameReportVO> queryGameReport(GameRebateDataQueryDTO dto);
}
