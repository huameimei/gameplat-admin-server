package com.gameplat.admin.service.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;

import com.gameplat.admin.service.GameConfigService;
import com.gameplat.admin.service.HGSportService;
import com.gameplat.base.common.context.DyDataSourceContextHolder;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.game.api.hg.bean.HGConfig;
import com.gameplat.common.game.api.hg.enums.API;
import com.gameplat.common.game.api.hg.enums.HGEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author aBen
 * @date 2022/1/6 0:06
 * @desc
 */
@Slf4j
@Service
public class HGSportServiceImpl implements HGSportService {

    @Autowired
    private GameConfigService gameConfigService;

    @Override
    public JSONObject queryHGBetOrder(JSONObject param) {
        return doGetRequest(API.HG_BET_ORDER.getUrl(), param, API.HG_BET_ORDER.getName());
    }

    @Override
    public JSONObject queryHGBetLimitList(JSONObject param) {
        return doGetRequest(API.HG_BET_LIMIT_LIST.getUrl(), param, API.HG_BET_LIMIT_LIST.getName());
    }

    @Override
    public JSONObject queryHGSportLeague(JSONObject param) {
        return doGetRequest(API.HG_SPORT_LEAGUE.getUrl(), param, API.HG_SPORT_LEAGUE.getName());
    }

    @Override
    public JSONObject queryHGSportResult(JSONObject param) {
        return doGetRequest(API.HG_SPORT_RESULT.getUrl(), param, API.HG_SPORT_RESULT.getName());
    }

    @Override
    public JSONObject queryHGCurBetOrder(JSONObject param) {
        return doGetRequest(API.HG_CUR_BET_ORDER.getUrl(), param, API.HG_CUR_BET_ORDER.getName());
    }

    @Override
    public JSONObject queryHGBetsDetail(JSONObject param) {
        return doGetRequest(API.HG_BETS_DETAIL.getUrl(), param, API.HG_BETS_DETAIL.getName());
    }

    @Override
    public JSONObject queryHGBetConfigs(JSONObject param) {
        return doGetRequest(API.HG_BET_CONFIGS.getUrl(), param, API.HG_BET_CONFIGS.getName());
    }

    @Override
    public JSONObject queryHGEntryResult(JSONObject param) {
        return doGetRequest(API.HG_ENTRY_RESULT.getUrl(), param, API.HG_ENTRY_RESULT.getName());
    }

    @Override
    public JSONObject queryHGUserLimit(JSONObject param) {
        return doGetRequest(API.HG_USER_LIMIT.getUrl(), param, API.HG_USER_LIMIT.getName());
    }

    @Override
    public JSONObject queryHGSportMessage(JSONObject param) {
        return doGetRequest(API.HG_SPORT_MESSAGE.getUrl(), param, API.HG_SPORT_MESSAGE.getName());
    }

    @Override
    public JSONObject queryHGChangeNotice(JSONObject param) {
        return doGetRequest(API.HG_CHANGE_NOTICE.getUrl(), param, API.HG_CHANGE_NOTICE.getName());
    }

    @Override
    public JSONObject queryHGHandOrdersDetail(JSONObject param) {
        return doGetRequest(API.QUERY_HAND_ORDERS_DETAIL.getUrl(), param, API.QUERY_HAND_ORDERS_DETAIL.getName());
    }

    @Override
    public JSONObject updateHgConfigModify(JSONObject param) {
        return doPostRequest(API.HG_CONFIG_MODIFY.getUrl(), param, API.HG_CONFIG_MODIFY.getName());
    }

    @Override
    public JSONObject queryHgSportConfig(JSONObject param) {
        return doGetRequest(API.HG_SPORT_CONFIG.getUrl(), param, API.HG_SPORT_CONFIG.getName());
    }

    @Override
    public JSONObject inputResultForFoot(JSONObject param) {
        return doPostRequest(API.HG_INPUT_RESULT_FOR_FOOT.getUrl(), param, API.HG_INPUT_RESULT_FOR_FOOT.getName());
    }

    @Override
    public JSONObject inputResultForBasketball(JSONObject param) {
        return doPostRequest(API.HG_INPUT_RESULT_FOR_BASKETBALL.getUrl(), param, API.HG_INPUT_RESULT_FOR_BASKETBALL.getName());
    }

    @Override
    public JSONObject updateSportStatus(JSONObject param) {
        return doPostRequest(API.HG_UPDATE_SPORT_STATUS.getUrl(), param, API.HG_UPDATE_SPORT_STATUS.getName());
    }

    @Override
    public JSONObject updateSportBetLimitMoney(JSONObject param) {
        return doPostRequest(API.HG_UPDATE_SPORT_BET_LIMIT_MONEY.getUrl(), param, API.HG_UPDATE_SPORT_BET_LIMIT_MONEY.getName());
    }

    @Override
    public JSONObject updateBetConfig(JSONObject param) {
        return doPostRequest(API.HG_UPDATE_BET_CONFIG.getUrl(), param, API.HG_UPDATE_BET_CONFIG.getName());
    }

    @Override
    public JSONObject betUpdateActions(JSONObject param) {
        return doPostRequest(API.HG_BET_UPDATE_ACTIONS.getUrl(), param, API.HG_BET_UPDATE_ACTIONS.getName());
    }

    @Override
    public JSONObject batchActionsByStatus(JSONObject param) {
        return doPostRequest(API.HG_BATCH_ACTIONS_BY_STATUS.getUrl(), param, API.HG_BATCH_ACTIONS_BY_STATUS.getName());
    }

    @Override
    public JSONObject settlingResultForFoot(JSONObject param) {
        return doPostRequest(API.HG_SETTLING_RESULT_FOR_FOOT.getUrl(), param, API.HG_SETTLING_RESULT_FOR_FOOT.getName());
    }

    @Override
    public JSONObject settlingResultForBasketball(JSONObject param) {
        return doPostRequest(API.HG_SETTLING_RESULT_FOR_BASKETBALL.getUrl(), param, API.HG_SETTLING_RESULT_FOR_BASKETBALL.getName());
    }

    @Override
    public JSONObject saveUserLimit(JSONObject param) {
        return doPostRequest(API.HG_SAVE_USER_LIMIT.getUrl(), param, API.HG_SAVE_USER_LIMIT.getName());
    }

    @Override
    public JSONObject updateUserLimit(JSONObject param) {
        return doPostRequest(API.HG_UPDATE_USER_LIMIT.getUrl(), param, API.HG_UPDATE_USER_LIMIT.getName());
    }

    @Override
    public JSONObject removeUserLimit(JSONObject param) {
        return doPostRequest(API.HG_REMOVE_USER_LIMIT.getUrl(), param, API.HG_REMOVE_USER_LIMIT.getName());
    }

    @Override
    public JSONObject saveSportMessage(JSONObject param) {
        return doPostRequest(API.HG_SAVE_SPORT_MESSAGE.getUrl(), param, API.HG_SAVE_SPORT_MESSAGE.getName());
    }

    @Override
    public JSONObject removeSportMessage(JSONObject param) {
        return doPostRequest(API.HG_REMOVE_SPORT_MESSAGE.getUrl(), param, API.HG_REMOVE_SPORT_MESSAGE.getName());
    }

    @Override
    public JSONObject modifySportMessage(JSONObject param) {
        return doPostRequest(API.HG_MODIFY_SPORT_MESSAGE.getUrl(), param, API.HG_MODIFY_SPORT_MESSAGE.getName());
    }

    @Override
    public JSONObject sportChangeNotice(JSONObject param) {
        return doPostRequest(API.HG_SPORT_CHANGE_NOTICE.getUrl(), param, API.HG_SPORT_CHANGE_NOTICE.getName());
    }

    @Override
    public JSONObject queryHGConfigGetOne(JSONObject param) {
        return doGetRequest(API.HG_CONFIG_GET_ONE.getUrl(), param, API.HG_CONFIG_GET_ONE.getName());
    }

    public JSONObject doGetRequest(String apiUrl, JSONObject param, String describe) {
        JSONObject configJson = gameConfigService.queryGameConfigInfoByPlatCode(GamePlatformEnum.HG.getName());
        if (StringUtils.isNull(configJson)) {
            throw new ServiceException("未找到皇冠体育游戏配置");
        }
        HGConfig hgConfig = JSONObject.parseObject(configJson.toJSONString(), HGConfig.class);
        String url = hgConfig.getHost() + apiUrl;
        log.info("获取HG{}，请求地址:{}，请求参数:{}", describe, url, param);
        String result = HttpRequest.get(url).header("tenant", DyDataSourceContextHolder.getTenant()).form(param).execute().body();
        log.info("获取HG{}，请求响应:{}", describe, result);
        if (StringUtils.isEmpty(result)) {
            throw new ServiceException("皇冠体育请求响应为空");
        }
        return JSONObject.parseObject(result);
    }

    public JSONObject doPostRequest(String apiUrl, JSONObject param, String describe) {
        JSONObject configJson = gameConfigService.queryGameConfigInfoByPlatCode(GamePlatformEnum.HG.getName());
        if (StringUtils.isNull(configJson)) {
            throw new ServiceException("未找到皇冠体育游戏配置");
        }
        HGConfig hgConfig = JSONObject.parseObject(configJson.toJSONString(), HGConfig.class);
        String url = hgConfig.getHost() + apiUrl;
        log.info("获取HG{}，请求地址:{}，请求参数:{}", describe, url, param);
        String result = HttpRequest.post(url).header("tenant", DyDataSourceContextHolder.getTenant()).form(param).execute().body();
        log.info("获取HG{}，请求响应:{}", describe, result);
        if (StringUtils.isEmpty(result)) {
            throw new ServiceException("皇冠体育请求响应为空");
        }
        return JSONObject.parseObject(result);
    }


}
