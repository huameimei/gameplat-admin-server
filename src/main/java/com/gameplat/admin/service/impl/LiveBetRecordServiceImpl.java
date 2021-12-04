package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gameplat.admin.enums.LiveGame;
import com.gameplat.admin.model.dto.LiveBetRecordQueryDTO;
import com.gameplat.admin.model.vo.LiveBetRecordVO;
import com.gameplat.admin.model.vo.LiveGameResultVO;
import com.gameplat.admin.model.vo.PageDtoVO;
import com.gameplat.admin.service.LiveBetRecordService;
import com.gameplat.base.common.exception.ServiceException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class LiveBetRecordServiceImpl implements LiveBetRecordService {

  @Override
  public PageDtoVO<LiveBetRecordVO> queryPageBetRecord(Page<LiveBetRecordVO> page, LiveBetRecordQueryDTO dto) {
    //TODO 调用ES服务
    PageDtoVO<LiveBetRecordVO> pageDtoVO = new PageDtoVO();
    Map<String, Object> otherData = new HashMap<>();
    otherData.put("totalData", null);
    pageDtoVO.setPage(null);
    pageDtoVO.setOtherData(otherData);
    return pageDtoVO;
  }

  @Override
  public LiveGameResultVO getGameResult(String liveCode, String billNo) {
    // TODO 直接连游戏查询结果
    //GameApi gameApi = getGameApi(liveCode);
    // gameApi.getGameResult(billNo);
    LiveGameResultVO liveGameResult = null;
    if (StringUtils.isBlank(liveGameResult.getData())) {
      throw new ServiceException(LiveGame.getName(liveCode) + "暂不支持查看游戏结果");
    }
    return liveGameResult;
  }
}
