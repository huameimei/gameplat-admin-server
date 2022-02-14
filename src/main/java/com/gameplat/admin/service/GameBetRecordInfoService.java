package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.model.bean.ActivityStatisticItem;
import com.gameplat.admin.model.dto.GameBetRecordQueryDTO;
import com.gameplat.admin.model.vo.GameBetRecordVO;
import com.gameplat.admin.model.vo.PageDtoVO;

import com.gameplat.common.game.LiveGameResult;
import java.util.List;
import java.util.Map;

public interface GameBetRecordInfoService {

    PageDtoVO<GameBetRecordVO> queryPageBetRecord(Page<GameBetRecordVO> page, GameBetRecordQueryDTO dto);

    LiveGameResult getGameResult(GameBetRecordQueryDTO dto) throws Exception;

    List<ActivityStatisticItem> xjAssignMatchDml(Map map);
}
