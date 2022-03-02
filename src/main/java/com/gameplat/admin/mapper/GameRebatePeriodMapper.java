package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.vo.GameRebatePeriodVO;
import com.gameplat.model.entity.game.GameRebatePeriod;

import java.util.List;

public interface GameRebatePeriodMapper extends BaseMapper<GameRebatePeriod> {

  List<GameRebatePeriodVO> queryGameRebateCount();
}
