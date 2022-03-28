package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.model.bean.Language;
import com.gameplat.admin.model.vo.ConfigVO;
import com.gameplat.admin.service.CommonService;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CommonServiceImpl implements CommonService {

  @Autowired private SysDictDataService dictDataService;

  @Autowired private ConfigService configService;

  @Autowired private LimitInfoService limitInfoService;

  @Override
  @SentinelResource(value = "getLoginLimit")
  public AdminLoginLimit getLoginLimit() {
    return limitInfoService.getAdminLimit();
  }

  @Override
  @SentinelResource(value = "getDictByTypes")
  public Map<Object, List<JSONObject>> getDictByTypes(String types) {
    return dictDataService.getDictDataByTypes(Arrays.asList(types.split(","))).stream()
        .map(
            item -> {
              JSONObject jsonObject = new JSONObject();
              jsonObject.put("label", item.getDictLabel());
              jsonObject.put("value", item.getDictValue());
              jsonObject.put("key", item.getDictType());
              jsonObject.put("name", item.getDictName());
              return jsonObject;
            })
        .collect(Collectors.groupingBy(item -> item.get("key")));
  }

  @Override
  @SentinelResource(value = "getConfig")
  public ConfigVO getConfig() {
    List<Language> languages =
        configService.get(DictDataEnum.LANGUAGE, new TypeReference<List<Language>>() {});
    return ConfigVO.builder().loginConfig(this.getLoginLimit()).languages(languages).build();
  }
}
