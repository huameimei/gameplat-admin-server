package com.gameplat.admin.service;

import com.gameplat.admin.model.dto.HGSportDTO;

/**
 * @author aBen
 * @date 2022/1/6 0:05
 * @desc
 */
public interface HGSportService {

    Object queryHGBetOrder(HGSportDTO dto);

    Object queryHGBetLimitList(HGSportDTO dto);

    Object queryHGSportLeague(HGSportDTO dto);

    Object queryHGSportResult(HGSportDTO dto);

    Object queryHGCurBetOrder(HGSportDTO dto);

    Object queryHGBetsDetail(HGSportDTO dto);

    Object queryHGBetConfigs(HGSportDTO dto);

    Object queryHGEntryResult(HGSportDTO dto);

    Object queryHGUserLimit(HGSportDTO dto);

    Object queryHGSportMessage(HGSportDTO dto);

    Object queryHGChangeNotice(HGSportDTO dto);

    Object queryHGHandOrdersDetail(HGSportDTO dto);

    Object updateHgConfigModify(HGSportDTO dto);

    Object queryHgSportConfig(HGSportDTO dto);

    Object inputResultForFoot(HGSportDTO dto);

    Object inputResultForBasketball(HGSportDTO dto);

    Object updateSportStatus(HGSportDTO dto);

    Object updateSportBetLimitMoney(HGSportDTO dto);

    Object updateBetConfig(HGSportDTO dto);

    Object betUpdateActions(HGSportDTO dto);

    Object batchActionsByStatus(HGSportDTO dto);

    Object settlingResultForFoot(HGSportDTO dto);

    Object settlingResultForBasketball(HGSportDTO dto);

    Object saveUserLimit(HGSportDTO dto);

    Object updateUserLimit(HGSportDTO dto);

    Object removeUserLimit(HGSportDTO dto);

    Object saveSportMessage(HGSportDTO dto);

    Object removeSportMessage(HGSportDTO dto);

    Object modifySportMessage(HGSportDTO dto);

    Object sportChangeNotice(HGSportDTO dto);

    Object queryHGConfigGetOne(HGSportDTO dto);

}
