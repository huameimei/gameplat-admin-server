package com.gameplat.admin.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.gameplat.admin.enums.DictTypeEnum;
import com.gameplat.admin.mapper.SysUserMapper;
import com.gameplat.admin.model.bean.AdminLoginLimit;
import com.gameplat.admin.model.bean.Language;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.GoogleAuthDTO;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.util.GoogleAuthenticator;
import com.gameplat.base.common.util.StringUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公共服务功能服务
 *
 * @author three
 */
@Slf4j
@Service
public class SysCommonService {

  @Autowired private SysDictDataService dictDataService;

  @Autowired private SysUserMapper userMapper;

  /**
   * 获取登录限制
   *
   * @return
   */
  @SentinelResource(value = "getLoginLimit")
  public AdminLoginLimit getLoginLimit() {
    return dictDataService.getDictData(DictTypeEnum.ADMIN_LOGIN_CONFIG, AdminLoginLimit.class);
  }

  @SentinelResource(value = "getDictByTypes")
  public Map<Object, List<JSONObject>> getDictByTypes(String types) {
    return dictDataService.getDictDataByTypes(Arrays.asList(types.split(","))).stream()
        .map(
            item -> {
              JSONObject jsonObject = new JSONObject();
              jsonObject.put("label", item.getDictLabel());
              jsonObject.put("value", item.getDictValue());
              jsonObject.put("key", item.getDictType());
              return jsonObject;
            })
        .collect(Collectors.groupingBy(item -> item.get("key")));
  }

  /**
   * 检查谷歌验证是否开启
   *
   * @return
   */
  @SentinelResource(value = "checkAuth")
  public AdminLoginLimit checkAuth() {
    return dictDataService.getDictData(DictTypeEnum.ADMIN_LOGIN_CONFIG, AdminLoginLimit.class);
  }

  /**
   * 获取平台语言配置
   *
   * @return
   */
  @SentinelResource(value = "language")
  public List<Language> language() {
    SysDictData dictData = dictDataService.getDictDataByType(DictTypeEnum.LANGUAGE_CONFIG.getKey());
    if (StringUtils.isNotNull(dictData)) {
      return JSONObject.parseArray(dictData.getDictValue(), Language.class);
    }
    return null;
  }

  /**
   * 用户绑定谷歌密钥
   *
   * @param authDTO
   * @return
   */
  @SentinelResource(value = "bindSecret")
  public void bindSecret(GoogleAuthDTO authDTO) {
    // 先校验验证码是否正确
    Boolean authcode = GoogleAuthenticator.authcode(authDTO.getSafeCode(), authDTO.getSecret());
    if (!authcode) {
      throw new ServiceException("安全码错误");
    }
    SysUser user = userMapper.selectUserByUserName(authDTO.getLoginName());
    if (StringUtils.isNull(user)) {
      throw new ServiceException("账号错误");
    }

    int row =
        userMapper.updateById(
            new SysUser() {
              {
                setUserId(user.getUserId());
                setSafeCode(authDTO.getSecret());
              }
            });
    if (row <= 0) {
      throw new ServiceException("绑定失败!");
    }
  }
}
