package com.gameplat.admin.service.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.model.dto.HGSportDTO;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.admin.service.HGSportService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.GamePlatformEnum;
import com.gameplat.common.game.api.hg.config.HGConfig;
import com.gameplat.common.game.api.hg.enums.API;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author aBen
 * @date 2022/1/6 0:06
 * @desc
 */
@Slf4j
@Service
public class HGSportServiceImpl implements HGSportService {

  @Autowired private GameConfigService gameConfigService;

  @Override
  public Object queryHGBetOrder(HGSportDTO dto) {
    return doGetRequest(API.HG_BET_ORDER.getUrl(), dto, API.HG_BET_ORDER.getName());
  }

  @Override
  public Object queryHGBetLimitList(HGSportDTO dto) {
    return doGetRequest(API.HG_BET_LIMIT_LIST.getUrl(), dto, API.HG_BET_LIMIT_LIST.getName());
  }

  @Override
  public Object queryHGSportLeague(HGSportDTO dto) {
    return doGetRequest(API.HG_SPORT_LEAGUE.getUrl(), dto, API.HG_SPORT_LEAGUE.getName());
  }

  @Override
  public Object queryHGSportResult(HGSportDTO dto) {
    return doGetRequest(API.HG_SPORT_RESULT.getUrl(), dto, API.HG_SPORT_RESULT.getName());
  }

  @Override
  public Object queryHGCurBetOrder(HGSportDTO dto) {
    return doGetRequest(API.HG_CUR_BET_ORDER.getUrl(), dto, API.HG_CUR_BET_ORDER.getName());
  }

  @Override
  public Object queryHGBetsDetail(HGSportDTO dto) {
    return doGetRequest(API.HG_BETS_DETAIL.getUrl(), dto, API.HG_BETS_DETAIL.getName());
  }

  @Override
  public Object queryHGBetConfigs(HGSportDTO dto) {
    return doGetRequest(API.HG_BET_CONFIGS.getUrl(), dto, API.HG_BET_CONFIGS.getName());
  }

  @Override
  public Object queryHGEntryResult(HGSportDTO dto) {
    return doGetRequest(API.HG_ENTRY_RESULT.getUrl(), dto, API.HG_ENTRY_RESULT.getName());
  }

  @Override
  public Object queryHGUserLimit(HGSportDTO dto) {
    return doGetRequest(API.HG_USER_LIMIT.getUrl(), dto, API.HG_USER_LIMIT.getName());
  }

  @Override
  public Object queryHGSportMessage(HGSportDTO dto) {
    return doGetRequest(API.HG_SPORT_MESSAGE.getUrl(), dto, API.HG_SPORT_MESSAGE.getName());
  }

  @Override
  public Object queryHGChangeNotice(HGSportDTO dto) {
    return doGetRequest(API.HG_CHANGE_NOTICE.getUrl(), dto, API.HG_CHANGE_NOTICE.getName());
  }

  @Override
  public Object queryHGHandOrdersDetail(HGSportDTO dto) {
    return doGetRequest(
        API.QUERY_HAND_ORDERS_DETAIL.getUrl(), dto, API.QUERY_HAND_ORDERS_DETAIL.getName());
  }

  @Override
  public Object updateHgConfigModify(HGSportDTO dto) {
    return doPostRequest(API.HG_CONFIG_MODIFY.getUrl(), dto, API.HG_CONFIG_MODIFY.getName());
  }

  @Override
  public Object queryHgSportConfig(HGSportDTO dto) {
    return doGetRequest(API.HG_SPORT_CONFIG.getUrl(), dto, API.HG_SPORT_CONFIG.getName());
  }

  @Override
  public Object inputResultForFoot(HGSportDTO dto) {
    return doPostRequest(
        API.HG_INPUT_RESULT_FOR_FOOT.getUrl(), dto, API.HG_INPUT_RESULT_FOR_FOOT.getName());
  }

  @Override
  public Object inputResultForBasketball(HGSportDTO dto) {
    return doPostRequest(
        API.HG_INPUT_RESULT_FOR_BASKETBALL.getUrl(),
        dto,
        API.HG_INPUT_RESULT_FOR_BASKETBALL.getName());
  }

  @Override
  public Object updateSportStatus(HGSportDTO dto) {
    return doPostRequest(
        API.HG_UPDATE_SPORT_STATUS.getUrl(), dto, API.HG_UPDATE_SPORT_STATUS.getName());
  }

  @Override
  public Object updateSportBetLimitMoney(HGSportDTO dto) {
    return doPostRequest(
        API.HG_UPDATE_SPORT_BET_LIMIT_MONEY.getUrl(),
        dto,
        API.HG_UPDATE_SPORT_BET_LIMIT_MONEY.getName());
  }

  @Override
  public Object updateBetConfig(HGSportDTO dto) {
    return doPostRequest(
        API.HG_UPDATE_BET_CONFIG.getUrl(), dto, API.HG_UPDATE_BET_CONFIG.getName());
  }

  @Override
  public Object betUpdateActions(HGSportDTO dto) {
    return doPostRequest(
        API.HG_BET_UPDATE_ACTIONS.getUrl(), dto, API.HG_BET_UPDATE_ACTIONS.getName());
  }

  @Override
  public Object batchActionsByStatus(HGSportDTO dto) {
    return doPostRequest(
        API.HG_BATCH_ACTIONS_BY_STATUS.getUrl(), dto, API.HG_BATCH_ACTIONS_BY_STATUS.getName());
  }

  @Override
  public Object settlingResultForFoot(HGSportDTO dto) {
    return doPostRequest(
        API.HG_SETTLING_RESULT_FOR_FOOT.getUrl(), dto, API.HG_SETTLING_RESULT_FOR_FOOT.getName());
  }

  @Override
  public Object settlingResultForBasketball(HGSportDTO dto) {
    return doPostRequest(
        API.HG_SETTLING_RESULT_FOR_BASKETBALL.getUrl(),
        dto,
        API.HG_SETTLING_RESULT_FOR_BASKETBALL.getName());
  }

  @Override
  public Object saveUserLimit(HGSportDTO dto) {
    return doPostRequest(API.HG_SAVE_USER_LIMIT.getUrl(), dto, API.HG_SAVE_USER_LIMIT.getName());
  }

  @Override
  public Object updateUserLimit(HGSportDTO dto) {
    return doPostRequest(
        API.HG_UPDATE_USER_LIMIT.getUrl(), dto, API.HG_UPDATE_USER_LIMIT.getName());
  }

  @Override
  public Object removeUserLimit(HGSportDTO dto) {
    return doPostRequest(
        API.HG_REMOVE_USER_LIMIT.getUrl(), dto, API.HG_REMOVE_USER_LIMIT.getName());
  }

  @Override
  public Object saveSportMessage(HGSportDTO dto) {
    return doPostRequest(
        API.HG_SAVE_SPORT_MESSAGE.getUrl(), dto, API.HG_SAVE_SPORT_MESSAGE.getName());
  }

  @Override
  public Object removeSportMessage(HGSportDTO dto) {
    return doPostRequest(
        API.HG_REMOVE_SPORT_MESSAGE.getUrl(), dto, API.HG_REMOVE_SPORT_MESSAGE.getName());
  }

  @Override
  public Object modifySportMessage(HGSportDTO dto) {
    return doPostRequest(
        API.HG_MODIFY_SPORT_MESSAGE.getUrl(), dto, API.HG_MODIFY_SPORT_MESSAGE.getName());
  }

  @Override
  public Object sportChangeNotice(HGSportDTO dto) {
    return doPostRequest(
        API.HG_SPORT_CHANGE_NOTICE.getUrl(), dto, API.HG_SPORT_CHANGE_NOTICE.getName());
  }

  @Override
  public Object queryHGConfigGetOne(HGSportDTO dto) {
    return doGetRequest(API.HG_CONFIG_GET_ONE.getUrl(), dto, API.HG_CONFIG_GET_ONE.getName());
  }

  public Object doGetRequest(String apiUrl, HGSportDTO dto, String describe) {
    JSONObject configJson =
        gameConfigService.queryGameConfigInfoByPlatCode(GamePlatformEnum.HG.getName());
    if (StringUtils.isNull(configJson)) {
      throw new ServiceException("未找到皇冠体育游戏配置");
    }
    HGConfig hgConfig = JSONObject.parseObject(configJson.toJSONString(), HGConfig.class);
    String url = hgConfig.getHost() + apiUrl;
    JSONObject requestParam = JSONObject.parseObject(JSONObject.toJSONString(dto));
    requestParam.put("vendorId", hgConfig.getVendorId());
    requestParam.put("operatorId", hgConfig.getOperatorId());
    log.info("获取HG{}，请求地址:{}，请求参数:{}", describe, url, requestParam);
    String result =
        HttpRequest.get(url).header("tenant", "kguat").form(requestParam).execute().body();
    log.info("获取HG{}，请求响应:{}", describe, result);
    if (StringUtils.isEmpty(result)) {
      throw new ServiceException("皇冠体育请求响应为空");
    }
    JSONObject resultJson = JSONObject.parseObject(result);
    Integer code = resultJson.getInteger("code");
    String i18nKey = resultJson.getString("i18nKey");
    String message = resultJson.getString("message");
    if (StringUtils.isNotNull(code)) {
      if (code == 1) {
        if (resultJson.get("data") instanceof JSONArray) {
          return resultJson.getJSONArray("data");
        } else {
          return resultJson.getJSONObject("data");
        }
      } else if (code == 0 && StringUtils.isEmpty(i18nKey)) {
        throw new ServiceException(message);
      }
    }

    throw new ServiceException("皇冠体育请求响应异常");
  }

  public Object doPostRequest(String apiUrl, HGSportDTO dto, String describe) {
    JSONObject configJson =
        gameConfigService.queryGameConfigInfoByPlatCode(GamePlatformEnum.HG.getName());
    if (StringUtils.isNull(configJson)) {
      throw new ServiceException("未找到皇冠体育游戏配置");
    }
    HGConfig hgConfig = JSONObject.parseObject(configJson.toJSONString(), HGConfig.class);
    String url = hgConfig.getHost() + apiUrl;
    JSONObject requestParam = JSONObject.parseObject(JSONObject.toJSONString(dto));
    requestParam.put("vendorId", hgConfig.getVendorId());
    requestParam.put("operatorId", hgConfig.getOperatorId());
    log.info("获取HG{}，请求地址:{}，请求参数:{}", describe, url, requestParam);
    String result =
        HttpRequest.post(url).header("tenant", "kguat").form(requestParam).execute().body();
    log.info("获取HG{}，请求响应:{}", describe, result);
    if (StringUtils.isEmpty(result)) {
      throw new ServiceException("皇冠体育请求响应为空");
    }
    JSONObject resultJson = JSONObject.parseObject(result);
    Integer code = resultJson.getInteger("code");
    String i18nKey = resultJson.getString("i18nKey");
    String message = resultJson.getString("message");
    if (StringUtils.isNotNull(code)) {
      if (code == 1) {
        if (resultJson.get("data") instanceof JSONArray) {
          return resultJson.getJSONArray("data");
        } else {
          return resultJson.getJSONObject("data");
        }
      } else if (code == 0 && StringUtils.isEmpty(i18nKey)) {
        throw new ServiceException(message);
      }
    }

    throw new ServiceException("皇冠体育请求响应异常");
  }
}
