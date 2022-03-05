package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.model.entity.game.LiveBetRecord;

import java.util.List;
import java.util.Map;

public interface LiveBetRecordMapper extends BaseMapper<LiveBetRecord> {

  List<LiveBetRecord> queryGameBetRecords(Map map);
}
