package com.gameplat.admin.service.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.gameplat.admin.model.dto.HGSportDTO;
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
    public JSONObject queryHGBetOrder(HGSportDTO param) {
        return doGetRequest(API.HG_BET_ORDER.getUrl(), param, API.HG_BET_ORDER.getName(), 1);
    }

    @Override
    public JSONObject queryHGBetLimitList(HGSportDTO param) {
        return doGetRequest(API.HG_BET_LIMIT_LIST.getUrl(), param, API.HG_BET_LIMIT_LIST.getName(), 1);
    }

    @Override
    public JSONObject queryHGSportLeague(HGSportDTO param) {
        return doGetRequest(API.HG_SPORT_LEAGUE.getUrl(), param, API.HG_SPORT_LEAGUE.getName(), 1);
    }

    @Override
    public JSONObject queryHGSportResult(HGSportDTO param) {
        return doGetRequest(API.HG_SPORT_RESULT.getUrl(), param, API.HG_SPORT_RESULT.getName(), 1);
    }

    @Override
    public JSONObject queryHGCurBetOrder(HGSportDTO param) {
        return doGetRequest(API.HG_CUR_BET_ORDER.getUrl(), param, API.HG_CUR_BET_ORDER.getName(), 1);
    }

    @Override
    public JSONObject queryHGBetsDetail(HGSportDTO param) {
        return doGetRequest(API.HG_BETS_DETAIL.getUrl(), param, API.HG_BETS_DETAIL.getName(), 1);
    }

    @Override
    public JSONObject queryHGBetConfigs(HGSportDTO param) {
        return doGetRequest(API.HG_BET_CONFIGS.getUrl(), param, API.HG_BET_CONFIGS.getName(), 2);
    }

    @Override
    public JSONObject queryHGEntryResult(HGSportDTO param) {
        JSONObject requestJSON = doGetRequest(API.HG_ENTRY_RESULT.getUrl(), param, API.HG_ENTRY_RESULT.getName(), 1);
        if (requestJSON.getInteger("code") == 200) {
            JSONArray jsonArray = requestJSON.getJSONArray("data");
            int i = 1;
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                jsonObject.put("sole", i++);
            }
        }
        return requestJSON;
    }

    @Override
    public JSONObject queryHGUserLimit(HGSportDTO param) {
        JSONObject requestJSON = doGetRequest(API.HG_USER_LIMIT.getUrl(), param, API.HG_USER_LIMIT.getName(), 2);
        JSONObject data = requestJSON.getJSONObject("data");
        data.put("username", param.getUsername());
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(data);
        requestJSON.put("data", jsonArray);
        return requestJSON;
    }

    @Override
    public JSONObject queryHGSportMessage(HGSportDTO param) {
        return doGetRequest(API.HG_SPORT_MESSAGE.getUrl(), param, API.HG_SPORT_MESSAGE.getName(), 1);
    }

    @Override
    public JSONObject queryHGChangeNotice(HGSportDTO param) {
        return doGetRequest(API.HG_CHANGE_NOTICE.getUrl(), param, API.HG_CHANGE_NOTICE.getName(), 1);
    }

    @Override
    public JSONObject queryHGHandOrdersDetail(HGSportDTO param) {
        return doGetRequest(API.QUERY_HAND_ORDERS_DETAIL.getUrl(), param, API.QUERY_HAND_ORDERS_DETAIL.getName(), 1);
    }

    @Override
    public JSONObject updateHgConfigModify(HGSportDTO param) {
        return doPostRequest(API.HG_CONFIG_MODIFY.getUrl(), param, API.HG_CONFIG_MODIFY.getName(), 2);
    }

    @Override
    public JSONObject queryHgSportConfig(HGSportDTO param) {
        return doGetRequest(API.HG_SPORT_CONFIG.getUrl(), param, API.HG_SPORT_CONFIG.getName(), 2);
    }

    @Override
    public JSONObject inputResultForFoot(HGSportDTO param) {
        return doPostRequest(API.HG_INPUT_RESULT_FOR_FOOT.getUrl(), param, API.HG_INPUT_RESULT_FOR_FOOT.getName(), 2);
    }

    @Override
    public JSONObject inputResultForBasketball(HGSportDTO param) {
        return doPostRequest(API.HG_INPUT_RESULT_FOR_BASKETBALL.getUrl(), param, API.HG_INPUT_RESULT_FOR_BASKETBALL.getName(), 2);
    }

    @Override
    public JSONObject updateSportStatus(HGSportDTO param) {
        return doPostRequest(API.HG_UPDATE_SPORT_STATUS.getUrl(), param, API.HG_UPDATE_SPORT_STATUS.getName(), 2);
    }

    @Override
    public JSONObject updateSportBetLimitMoney(HGSportDTO param) {
        return doPostRequest(API.HG_UPDATE_SPORT_BET_LIMIT_MONEY.getUrl(), param, API.HG_UPDATE_SPORT_BET_LIMIT_MONEY.getName(), 2);
    }

    @Override
    public JSONObject updateBetConfig(HGSportDTO param) {
        return doPostRequest(API.HG_UPDATE_BET_CONFIG.getUrl(), param, API.HG_UPDATE_BET_CONFIG.getName(), 2);
    }

    @Override
    public JSONObject betUpdateActions(HGSportDTO param) {
        return doPostRequest(API.HG_BET_UPDATE_ACTIONS.getUrl(), param, API.HG_BET_UPDATE_ACTIONS.getName(), 2);
    }

    @Override
    public JSONObject batchActionsByStatus(HGSportDTO param) {
        return doPostRequest(API.HG_BATCH_ACTIONS_BY_STATUS.getUrl(), param, API.HG_BATCH_ACTIONS_BY_STATUS.getName(), 2);
    }

    @Override
    public JSONObject settlingResultForFoot(HGSportDTO param) {
        return doPostRequest(API.HG_SETTLING_RESULT_FOR_FOOT.getUrl(), param, API.HG_SETTLING_RESULT_FOR_FOOT.getName(), 2);
    }

    @Override
    public JSONObject settlingResultForBasketball(HGSportDTO param) {
        return doPostRequest(API.HG_SETTLING_RESULT_FOR_BASKETBALL.getUrl(), param, API.HG_SETTLING_RESULT_FOR_BASKETBALL.getName(), 2);
    }

    @Override
    public JSONObject saveUserLimit(HGSportDTO param) {
        return doPostRequest(API.HG_SAVE_USER_LIMIT.getUrl(), param, API.HG_SAVE_USER_LIMIT.getName(), 2);
    }

    @Override
    public JSONObject updateUserLimit(HGSportDTO param) {
        return doPostRequest(API.HG_UPDATE_USER_LIMIT.getUrl(), param, API.HG_UPDATE_USER_LIMIT.getName(), 2);
    }

    @Override
    public JSONObject removeUserLimit(HGSportDTO param) {
        return doPostRequest(API.HG_REMOVE_USER_LIMIT.getUrl(), param, API.HG_REMOVE_USER_LIMIT.getName(), 2);
    }

    @Override
    public JSONObject saveSportMessage(HGSportDTO param) {
        return doPostRequest(API.HG_SAVE_SPORT_MESSAGE.getUrl(), param, API.HG_SAVE_SPORT_MESSAGE.getName(), 2);
    }

    @Override
    public JSONObject removeSportMessage(HGSportDTO param) {
        return doPostRequest(API.HG_REMOVE_SPORT_MESSAGE.getUrl(), param, API.HG_REMOVE_SPORT_MESSAGE.getName(), 2);
    }

    @Override
    public JSONObject modifySportMessage(HGSportDTO param) {
        return doPostRequest(API.HG_MODIFY_SPORT_MESSAGE.getUrl(), param, API.HG_MODIFY_SPORT_MESSAGE.getName(), 2);
    }

    @Override
    public JSONObject sportChangeNotice(HGSportDTO param) {
        return doPostRequest(API.HG_SPORT_CHANGE_NOTICE.getUrl(), param, API.HG_SPORT_CHANGE_NOTICE.getName(), 2);
    }

    @Override
    public JSONObject queryHGConfigGetOne(HGSportDTO param) {
        return doGetRequest(API.HG_CONFIG_GET_ONE.getUrl(), param, API.HG_CONFIG_GET_ONE.getName(), 2);
    }

    public JSONObject doGetRequest(String apiUrl, HGSportDTO param, String describe, Integer flag) {
        JSONObject resultJson = new JSONObject();
        try {
            JSONObject configJson = gameConfigService.queryGameConfigInfoByPlatCode(GamePlatformEnum.HG.getName());
            if (StringUtils.isNull(configJson)) {
                throw new ServiceException("未找到皇冠体育游戏配置");
            }
            HGConfig hgConfig = JSONObject.parseObject(configJson.toJSONString(), HGConfig.class);
            Map<String, Object> map = hgConfig.basePrams(JSONObject.parseObject(JSONObject.toJSONString(param)), hgConfig, 1);
            String url = hgConfig.getHost() + apiUrl;
            log.info("获取HG{}，请求地址:{}，请求参数:{}", describe, url, map);
            String result = HttpRequest.get(url).header("tenant", DyDataSourceContextHolder.getTenant()).form(map).execute().body();
            log.info("获取HG{}，请求响应:{}", describe, result);
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject.getInteger("code") == 1) {
                    if (flag == 1) {
                        getResultJsonByRecords(resultJson, jsonObject);
                    } else {
                        getResultJsonByData(resultJson, jsonObject);
                    }
                    return resultJson;
                } else if (jsonObject.getInteger("code") == 0) {
                    resultJson.put("code", 400);
                    resultJson.put("data", null);
                    resultJson.put("otherData", null);
                    resultJson.put("message", jsonObject.getString("message"));
                    resultJson.put("total", 0);
                    return resultJson;
                }
            }
        } catch (Exception e) {
            log.info("获取HG" + describe + "异常，异常原因：", e);
        }
        resultJson.put("code", 400);
        resultJson.put("data", null);
        resultJson.put("otherData", null);
        resultJson.put("total", 0);
        return resultJson;
    }

    public JSONObject doPostRequest(String apiUrl, HGSportDTO param, String describe, Integer flag) {
        JSONObject resultJson = new JSONObject();
        try {
            JSONObject configJson = gameConfigService.queryGameConfigInfoByPlatCode(GamePlatformEnum.HG.getName());
            if (StringUtils.isNull(configJson)) {
                throw new ServiceException("未找到皇冠体育游戏配置");
            }
            HGConfig hgConfig = JSONObject.parseObject(configJson.toJSONString(), HGConfig.class);
            Map<String, Object> map = hgConfig.basePrams(JSONObject.parseObject(JSONObject.toJSONString(param)), hgConfig, 2);
            String url = hgConfig.getHost() + apiUrl;
            log.info("获取HG{}，请求地址:{}，请求参数:{}", describe, url, map);
            String result = HttpRequest.post(url).header("tenant", DyDataSourceContextHolder.getTenant()).form(map).execute().body();
            log.info("获取HG{}，请求响应:{}", describe, result);
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (jsonObject.getInteger("code") == 1) {
                    if (flag == 1) {
                        getResultJsonByRecords(resultJson, jsonObject);
                    } else if (flag == 2) {
                        getResultJsonByData(resultJson, jsonObject);
                    }
                    return resultJson;
                } else if (jsonObject.getInteger("code") == 0) {
                    resultJson.put("code", 400);
                    resultJson.put("data", null);
                    resultJson.put("otherData", null);
                    resultJson.put("message", jsonObject.getString("message"));
                    resultJson.put("total", 0);
                    return resultJson;
                }
            }
        } catch (Exception e) {
            log.info("获取HG" + describe + "异常，异常原因：", e);
        }
        resultJson.put("code", 400);
        resultJson.put("data", null);
        resultJson.put("otherData", null);
        resultJson.put("message", "操作失败");
        resultJson.put("total", 0);
        return resultJson;
    }

    public void getResultJsonByRecords(JSONObject resultJson, JSONObject jsonObject) {
        JSONObject data = jsonObject.getJSONObject("data");
        resultJson.put("code", 200);
        resultJson.put("data", data.getJSONArray("records"));
        resultJson.put("otherData", data.getJSONObject("otherData"));
        resultJson.put("total", data.getInteger("total"));
    }

    public void getResultJsonByData(JSONObject resultJson, JSONObject jsonObject) {
        JSONArray dataJsonArray = null;
        JSONObject dataJsonObject = null;
        if (jsonObject.get("data") instanceof JSONArray) {
            dataJsonArray = jsonObject.getJSONArray("data");
        } else {
            dataJsonObject = jsonObject.getJSONObject("data");
        }
        resultJson.put("code", 200);
        if (StringUtils.isNotNull(dataJsonArray)) {
            resultJson.put("data", dataJsonArray);
        }
        if (StringUtils.isNotNull(dataJsonObject)) {
            resultJson.put("data", dataJsonObject);
            if (dataJsonObject.containsKey("otherData")) {
                resultJson.put("otherData", dataJsonObject.getJSONObject("otherData"));
            }
            if (dataJsonObject.containsKey("total")) {
                resultJson.put("total", dataJsonObject.getInteger("total"));
            }
        }

    }


}
