package com.gameplat.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.dto.HGSportDTO;

/**
 * @author aBen
 * @date 2022/1/6 0:05
 * @desc
 */
public interface HGSportService {

    JSONObject queryHGBetOrder(HGSportDTO param);

    JSONObject queryHGBetLimitList(HGSportDTO param);

    JSONObject queryHGSportLeague(HGSportDTO param);

    JSONObject queryHGSportResult(HGSportDTO param);

    JSONObject queryHGCurBetOrder(HGSportDTO param);

    JSONObject queryHGBetsDetail(HGSportDTO param);

    JSONObject queryHGBetConfigs(HGSportDTO param);

    JSONObject queryHGEntryResult(HGSportDTO param);

    JSONObject queryHGUserLimit(HGSportDTO param);

    JSONObject queryHGSportMessage(HGSportDTO param);

    JSONObject queryHGChangeNotice(HGSportDTO param);

    JSONObject queryHGHandOrdersDetail(HGSportDTO param);

    JSONObject updateHgConfigModify(HGSportDTO param);

    JSONObject queryHgSportConfig(HGSportDTO param);

    JSONObject inputResultForFoot(HGSportDTO param);

    JSONObject inputResultForBasketball(HGSportDTO param);

    JSONObject updateSportStatus(HGSportDTO param);

    JSONObject updateSportBetLimitMoney(HGSportDTO param);

    JSONObject updateBetConfig(HGSportDTO param);

    JSONObject betUpdateActions(HGSportDTO param);

    JSONObject batchActionsByStatus(HGSportDTO param);

    JSONObject settlingResultForFoot(HGSportDTO param);

    JSONObject settlingResultForBasketball(HGSportDTO param);

    JSONObject saveUserLimit(HGSportDTO param);

    JSONObject updateUserLimit(HGSportDTO param);

    JSONObject removeUserLimit(HGSportDTO param);

    JSONObject saveSportMessage(HGSportDTO param);

    JSONObject removeSportMessage(HGSportDTO param);

    JSONObject modifySportMessage(HGSportDTO param);

    JSONObject sportChangeNotice(HGSportDTO param);

    JSONObject queryHGConfigGetOne(HGSportDTO param);

}
