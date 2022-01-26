package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gameplat.admin.model.bean.Language;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.admin.model.vo.ConfigVO;
import com.gameplat.admin.service.*;
import com.gameplat.base.common.util.GoogleAuthenticator;
import com.gameplat.common.enums.DictDataEnum;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommonServiceImpl implements CommonService {

  @Autowired private SysDictDataService dictDataService;

  @Autowired private ConfigService configService;

  @Autowired private SysUserService userService;

  @Autowired private LimitInfoService limitInfoService;

  @Override
  @SentinelResource(value = "getLoginLimit")
  public AdminLoginLimit getLoginLimit() {
    return limitInfoService.getLimitInfo(LimitEnums.ADMIN_LOGIN_CONFIG, AdminLoginLimit.class);
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
    return ConfigVO.builder()
        .loginConfig(this.getLoginLimit())
        .languages(this.getLanguages())
        .build();
  }

  @Override
  @SentinelResource(value = "bindSecret")
  public void bindSecret(GoogleAuthDTO dto) {
    Boolean authCode = GoogleAuthenticator.authcode(dto.getSafeCode(), dto.getSecret());
    Assert.isTrue(authCode, "安全码错误!");

    SysUser user = Assert.notNull(userService.getByUsername(dto.getLoginName()), "账号错误!");
    user.setSafeCode(dto.getSecret());
    Assert.isTrue(userService.updateById(user), "绑定失败!");
  }

  /** 获取平台语言配置 */
  private List<Language> getLanguages() {
    return configService.get(DictDataEnum.LANGUAGE, new TypeReference<List<Language>>() {});
  }
}
