package com.gameplat.admin.service.impl;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.config.SysTheme;
import com.gameplat.admin.model.bean.SportGameConfig;
import com.gameplat.admin.model.bean.SportProperty;
import com.gameplat.admin.model.dto.LiveDomainParamDTO;
import com.gameplat.admin.service.GameConfigService;
import com.gameplat.admin.service.ILiveDomainService;
import com.gameplat.admin.util.SportRequestUtil;
import com.gameplat.base.common.util.StringUtils;
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
public class LiveDomainServiceImpl implements ILiveDomainService {

  @Autowired private GameConfigService gameConfigService;

  @Autowired private SportProperty sportProperty;

  @Autowired private SportRequestUtil requestUtil;

  @Autowired private SysTheme sysTheme;

  @Override
  public String getLiveDomainTrafficData(LiveDomainParamDTO param) {
    try {
      String tenant = sysTheme.getTenantCode();
      JSONObject sportConfigJson = gameConfigService.getGameConfig("SB");
      SportGameConfig config =
          JSONObject.parseObject(sportConfigJson.toJSONString(), SportGameConfig.class);
      Map<String, Object> paramMap = requestUtil.basePrams(param, config, 0);
      String url = sportProperty.getDomain() + sportProperty.getLiveDomainTraffic();
      log.info("主播流量使用和费用查询！ --- url:{} 请求参数:{}", url, paramMap.toString());
      String result =
          HttpRequest.post(url)
              .header("tenant", tenant)
              .header("country", param.getCountry() == null ? "zh-CN" : param.getCountry())
              .form(paramMap)
              .execute()
              .body();
      if (StringUtils.isNotEmpty(result)) {
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        if (StringUtils.isNotBlank(code) && "1".equals(code)) {
          return jsonObject.getString("data");
        } else {
          log.info("直播流量使用和费用查询失败！---  体育返回错误信息：" + jsonObject.getString("message"));
          return "";
        }
      } else {
        log.info("直播流量使用和费用查询失败！---  体育返回参数为空");
        return "";
      }
    } catch (Exception e) {
      log.info("直播流量使用和费用查询失败！{}", e);
      return "";
    }
  }

  @Override
  public Object getLiveDomainList(LiveDomainParamDTO param) {
    try {
      String tenant = sysTheme.getTenantCode();
      JSONObject sportConfigJson = gameConfigService.getGameConfig("SB");
      SportGameConfig config =
          JSONObject.parseObject(sportConfigJson.toJSONString(), SportGameConfig.class);
      Map<String, Object> paramMap = requestUtil.basePrams(param, config, 0);
      String url = sportProperty.getDomain() + sportProperty.getLiveDomainList();
      log.info("直播域名列表查询！ --- url:{} 请求参数:{}", url, paramMap.toString());
      String result =
          HttpRequest.post(url)
              .header("tenant", tenant)
              .header("country", param.getCountry() == null ? "zh-CN" : param.getCountry())
              .form(paramMap)
              .execute()
              .body();
      if (StringUtils.isNotEmpty(result)) {
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        JSONArray jsonArray = (JSONArray) jsonObject.get("data");
        if ("1".equals(code) && StringUtils.isNotNull(jsonArray)) {
          return jsonArray;
        } else {
          log.info("直播域名列表查询失败！---  体育返回错误信息：" + jsonObject.getString("message"));
        }
      } else {
        log.info("直播域名列表查询失败！---  体育返回参数为空");
      }
    } catch (Exception e) {
      log.info("直播域名列表查询失败！--- 报错信息{}", e);
      return null;
    }
    return null;
  }
}
