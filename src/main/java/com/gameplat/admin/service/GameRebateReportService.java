package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GameRebateReportQueryDTO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.model.entity.game.GameRebateDetail;
import com.gameplat.model.entity.game.GameRebatePeriod;
import com.gameplat.model.entity.game.GameRebateReport;

import java.math.BigDecimal;
import java.util.List;

public interface GameRebateReportService extends IService<GameRebateReport> {

  void accept(Long periodId, Long memberId, BigDecimal realRebateMoney, String remark) throws Exception;

  void deleteByPeriodId(Long periodId);

  void createForGameRebatePeriod(GameRebatePeriod liveRebatePeriod);

  void rollBack(
      Long memberId, Long periodId, String periodName, BigDecimal realRebateMoney, String remark);

  PageDtoVO<GameRebateDetail> queryPage(Page<GameRebateDetail> page, GameRebateReportQueryDTO dto);

  void reject(String account, String periodName, String remark);

  void modify(Long id, BigDecimal realRebateMoney, String remark);

  List<GameRebateReport> queryDetail(GameRebateReportQueryDTO dto);
}
