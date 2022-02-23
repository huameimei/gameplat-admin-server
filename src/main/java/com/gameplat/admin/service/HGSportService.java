package com.gameplat.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.dto.HGSportDTO;

/**
 * @author aBen
 * @date 2022/1/6 0:05
 * @desc
 */
public interface HGSportService {

    JSONObject queryHGBetOrder(HGSportDTO dto);

    JSONObject queryHGBetLimitList(HGSportDTO dto);

    JSONObject queryHGSportLeague(HGSportDTO dto);

    JSONObject queryHGSportResult(HGSportDTO dto);

    JSONObject queryHGCurBetOrder(HGSportDTO dto);

    JSONObject queryHGBetsDetail(HGSportDTO dto);

    JSONObject queryHGBetConfigs(HGSportDTO dto);

    JSONObject queryHGEntryResult(HGSportDTO dto);

    JSONObject queryHGUserLimit(HGSportDTO dto);

    JSONObject queryHGSportMessage(HGSportDTO dto);

    JSONObject queryHGChangeNotice(HGSportDTO dto);

    JSONObject queryHGHandOrdersDetail(HGSportDTO dto);

    JSONObject updateHgConfigModify(HGSportDTO dto);

    JSONObject queryHgSportConfig(HGSportDTO dto);

    JSONObject inputResultForFoot(HGSportDTO dto);

    JSONObject inputResultForBasketball(HGSportDTO dto);

    JSONObject updateSportStatus(HGSportDTO dto);

    JSONObject updateSportBetLimitMoney(HGSportDTO dto);

    JSONObject updateBetConfig(HGSportDTO dto);

    JSONObject betUpdateActions(HGSportDTO dto);

    JSONObject batchActionsByStatus(HGSportDTO dto);

    JSONObject settlingResultForFoot(HGSportDTO dto);

    JSONObject settlingResultForBasketball(HGSportDTO dto);

    JSONObject saveUserLimit(HGSportDTO dto);

    JSONObject updateUserLimit(HGSportDTO dto);

    JSONObject removeUserLimit(HGSportDTO dto);

    JSONObject saveSportMessage(HGSportDTO dto);

    JSONObject removeSportMessage(HGSportDTO dto);

    JSONObject modifySportMessage(HGSportDTO dto);

    JSONObject sportChangeNotice(HGSportDTO dto);

    JSONObject queryHGConfigGetOne(HGSportDTO dto);

}
