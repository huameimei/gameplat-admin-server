package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.dto.GameRWDataReportDto;
import com.gameplat.model.entity.game.GameTransferInfo;

import java.math.BigDecimal;

public interface GameTransferInfoMapper extends BaseMapper<GameTransferInfo> {

  void saveOrUpdate(GameTransferInfo gameTransferInfo);

  BigDecimal getAllGameBalance(GameRWDataReportDto dto);
}
