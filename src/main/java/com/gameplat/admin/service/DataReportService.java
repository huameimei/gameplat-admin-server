package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.dto.GameRWDataReportDto;
import com.gameplat.admin.model.vo.*;

import java.util.List;

/**
 * @author kb @Date 2022/3/2 21:49 @Version 1.0
 */
public interface DataReportService {

  GameRechDataReportVO findRechReport(GameRWDataReportDto dto);

  GameWithDataReportVO findWithReport(GameRWDataReportDto dto);

  GameDataReportVO findGameReport(GameRWDataReportDto dto);

  GameAccountDataReportVo findMemberReport(Page<AccountReportVo> page, GameRWDataReportDto dto);


  PageDtoVO findYubaoReportData(Page<YuBaoMemberBalanceVo> page, GameRWDataReportDto dto);

  GameDividendDataVo findDividendtDataReport(GameRWDataReportDto dto);

  PageDtoVO<AccountReportVo> findAccountReport(Page<AccountReportVo> page, GameRWDataReportDto dto);

  List<ThreeRechReportVo> findThreeRech(GameRWDataReportDto dto);

  GameProxyDataVo findProxyData(GameRWDataReportDto dto);

  PageDtoVO<GameRWDataVo> findRwData(Page<GameRWDataVo> page, GameRWDataReportDto dto);
}
