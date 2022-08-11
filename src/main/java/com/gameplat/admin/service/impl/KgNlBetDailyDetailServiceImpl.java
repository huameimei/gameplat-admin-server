package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.KgNlBetDailyDetailMapper;
import com.gameplat.admin.model.dto.KgNlWinReportQueryDTO;
import com.gameplat.admin.model.vo.KgNlWinReportVO;
import com.gameplat.admin.service.KgNlBetDailyDetailService;
import com.gameplat.model.entity.game.KgNlBetDailyDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class KgNlBetDailyDetailServiceImpl extends ServiceImpl<KgNlBetDailyDetailMapper, KgNlBetDailyDetail>
  implements KgNlBetDailyDetailService {

  @Autowired
  private KgNlBetDailyDetailMapper kgNlBetDailyDetailMapper;

  public List<KgNlWinReportVO> findWinReportData(KgNlWinReportQueryDTO dto){
    return kgNlBetDailyDetailMapper.findWinReportData(dto);
  }
}
