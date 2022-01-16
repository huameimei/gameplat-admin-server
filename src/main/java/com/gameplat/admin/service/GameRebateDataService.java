package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.GameRebateData;
import com.gameplat.admin.model.dto.GameRebateDataQueryDTO;
import com.gameplat.admin.model.vo.GameReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import java.util.List;

public interface GameRebateDataService extends IService<GameRebateData> {

  PageDtoVO queryPageData(Page<GameRebateData> page, GameRebateDataQueryDTO dto);

  void saveRebateReport(String statTime, GamePlatform gamePlatform);

  List<GameReportVO> queryGameReport(GameRebateDataQueryDTO dto);

}
