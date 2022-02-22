package com.gameplat.admin.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author aBen
 * @date 2022/1/6 0:05
 * @desc
 */
public interface HGSportService {

    JSONObject queryHGBetOrder(JSONObject param);

    JSONObject queryHGBetLimitList(JSONObject param);

    JSONObject queryHGSportLeague(JSONObject param);

    JSONObject queryHGSportResult(JSONObject param);

    JSONObject queryHGCurBetOrder(JSONObject param);

    JSONObject queryHGBetsDetail(JSONObject param);

    JSONObject queryHGBetConfigs(JSONObject param);

    JSONObject queryHGEntryResult(JSONObject param);

    JSONObject queryHGUserLimit(JSONObject param);

    JSONObject queryHGSportMessage(JSONObject param);

    JSONObject queryHGChangeNotice(JSONObject param);

    JSONObject queryHGHandOrdersDetail(JSONObject param);

    JSONObject updateHgConfigModify(JSONObject param);

    JSONObject queryHgSportConfig(JSONObject param);

    JSONObject inputResultForFoot(JSONObject param);

    JSONObject inputResultForBasketball(JSONObject param);

    JSONObject updateSportStatus(JSONObject param);

    JSONObject updateSportBetLimitMoney(JSONObject param);

    JSONObject updateBetConfig(JSONObject param);

    JSONObject betUpdateActions(JSONObject param);

    JSONObject batchActionsByStatus(JSONObject param);

    JSONObject settlingResultForFoot(JSONObject param);

    JSONObject settlingResultForBasketball(JSONObject param);

    JSONObject saveUserLimit(JSONObject param);

    JSONObject updateUserLimit(JSONObject param);

    JSONObject removeUserLimit(JSONObject param);

    JSONObject saveSportMessage(JSONObject param);

    JSONObject removeSportMessage(JSONObject param);

    JSONObject modifySportMessage(JSONObject param);

    JSONObject sportChangeNotice(JSONObject param);

    JSONObject queryHGConfigGetOne(JSONObject param);

}
