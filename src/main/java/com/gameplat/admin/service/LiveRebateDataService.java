package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GamePlatform;
import com.gameplat.admin.model.domain.LiveRebateData;
import com.gameplat.admin.model.dto.LiveRebateDataQueryDTO;
import com.gameplat.admin.model.vo.LiveReportVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import java.util.List;

public interface LiveRebateDataService  extends IService<LiveRebateData> {

  PageDtoVO queryPageData(Page<LiveRebateData> page, LiveRebateDataQueryDTO dto);

  void saveRebateReport(String statTime, GamePlatform gamePlatform);

  List<LiveReportVO> queryLiveReport(LiveRebateDataQueryDTO dto);

}
