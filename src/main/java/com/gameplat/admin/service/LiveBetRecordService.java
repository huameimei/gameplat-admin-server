package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.dto.LiveBetRecordQueryDTO;
import com.gameplat.admin.model.vo.LiveBetRecordVO;
import com.gameplat.admin.model.vo.PageDtoVO;

import com.gameplat.common.game.LiveGameResult;
import java.util.List;
import java.util.Map;

public interface LiveBetRecordService {

    PageDtoVO<LiveBetRecordVO> queryPageBetRecord(Page<LiveBetRecordVO> page, LiveBetRecordQueryDTO dto);

    LiveGameResult getGameResult(String liveCode, String billNo) throws Exception;

    List<ActivityStatisticItem> xjAssignMatchDml(Map map);
}
