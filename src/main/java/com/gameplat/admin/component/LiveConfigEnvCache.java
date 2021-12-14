package com.gameplat.admin.component;

import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.redis.api.RedisService;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Slf4j
@Component
public class LiveConfigEnvCache {
  //全局数据更新
  public static Map<String, String> data = new HashMap<>();

  @NacosValue(value = "platform.customCode",autoRefreshed = true)
  private String customCode;

  @NacosValue(value = "plaform.centerHost",autoRefreshed = true)
  private String centerHost;

  @NacosValue(value = "platform.localFilePath",autoRefreshed = true)
  private String  localFilePath;

  public final String cacheKey = "env_context_cache";

  @Resource
  private RedisService redisService;

  public static Map<String, String> getPros() {
    return new HashMap<>(data);
  }

  public static void setLiveContextData(Map<String, String> prosArgs) {
    data.putAll(prosArgs);
  }

  public static String getProsByName(String name) {
    return getProsByNameOfDefault(name, "");
  }

  public static String getProsByNameOfDefault(String name, String defaultValue) {
    return Optional.ofNullable(StringUtils.trimToNull(data.get(name))).orElse(defaultValue);
  }

  //@PostConstruct
  public void init(){
    //获取真人配置文件
    String json = (String) redisService.getStringOps().get(cacheKey);
    Map<String, String> context = Maps.newHashMap();
    try{
      context = checkAndParse(customCode,json);
    }catch (ServiceException redisDataError){
      log.error("Redis数据设置配置信息失败:[{}],尝试使用远程配置", redisDataError.getMessage());
      String configData;
      try {
        configData = loadRemotePros(centerHost,customCode);
        context = checkAndParse(customCode,configData);
      }catch (ServiceException remoteDataError){
        log.error("使用远程配置信息失败:[{}],尝试使用本地", remoteDataError.getMessage());
        try {
          String content = new String(Files
              .readAllBytes(Paths.get(Optional.ofNullable(localFilePath).orElse("/opt/apps/bootstart"))),
              StandardCharsets.UTF_8);
          if (StringUtils.isBlank(content)) {
            throw new ServiceException("系统本地启动文件没有信息");
          }
          configData = content.trim();
          context = checkAndParse(customCode, configData);
        } catch (IOException | ServiceException localDataError) {
          log.error("读取设置本地配置信息失败:{}", localDataError.getMessage());
          configData = null;
        }
      }
      if (StringUtils.isNotBlank(configData)) {
        redisService.getStringOps().set(cacheKey, configData);
      }
    }finally {
      setLiveContextData(context);
    }
  }

  public Map<String, String> checkAndParse(String code,String json) throws ServiceException {
    if (StringUtils.isBlank(json)) {
      throw new ServiceException("未获取到配置数据");
    }
    Map<String, String> context = JSONUtil.toBean(json,Map.class);
    if (MapUtils.isEmpty(context)) {
      throw new ServiceException("解析数据异常");
    }
    if (!code.equalsIgnoreCase(context.get("custom_code").trim())) {
      throw new ServiceException("数据令牌码与实际业主令牌码不一致!");
    }
    return context;
  }

  private String loadRemotePros(String host, String code) throws ServiceException {
    String url = String.format("http://%s/api/env/query/%s", host, code);
    int timeout = (int) TimeUnit.SECONDS.toMillis(20);
    HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(
        RequestConfig.custom().setConnectionRequestTimeout(timeout)
            .setConnectTimeout(timeout).setSocketTimeout(timeout).build()
    ).build();
    try {
      HttpResponse response = httpClient.execute(new HttpGet(url));
      if (response == null || response.getEntity() == null) {
        throw new IOException("response null");
      }
      String responseText = StreamUtils
          .copyToString(response.getEntity().getContent(), StandardCharsets.UTF_8);
      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode != HttpStatus.HTTP_OK) {
        throw new IOException("Exception statusCode=[" + statusCode + "] " + responseText);
      }
      return responseText;
    } catch (IOException e) {
      throw new ServiceException(String.format("request[%s] fail! %s", url, e.getMessage()));
    }
  }
}
