package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.model.entity.game.KgNlBetDailyDetail;

import java.util.List;

/**
 * @author aBen
 * @date 2022/4/17 19:34
 * @desc
 */
public interface KgNlBetDailyDetailService extends IService<KgNlBetDailyDetail> {

  List<KgNlWinReportVO> findWinReportData(KgNlWinReportQueryDTO dto);
}
