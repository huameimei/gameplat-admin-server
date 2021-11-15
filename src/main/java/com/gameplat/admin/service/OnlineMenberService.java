package com.gameplat.admin.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.cache.AdminCache;
import com.gameplat.admin.enums.DictTypeEnum;
import com.gameplat.admin.mapper.SysUserMapper;
import com.gameplat.admin.model.domain.SysDictData;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.OnlineUserDTO;
import com.gameplat.admin.model.vo.OnlineUserVo;
import com.gameplat.admin.model.vo.UserToken;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.util.StringUtils;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * 在线会员服务
 *
 * @author three
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OnlineMenberService {

  private final SysUserMapper userMapper;

  private final SysDictDataService dictDataService;

  private final AdminCache adminCache;

  private final JwtTokenService jwtTokenService;

  /**
   * 取在线用户列表
   *
   * @param userDTO
   * @return
   */
  @SentinelResource(value = "selectOnlineList")
  public OnlineUserVo selectOnlineList(PageDTO<UserToken> page, OnlineUserDTO userDTO) {
    SysUser user = userMapper.selectUserByUserName(SecurityUserHolder.getUsername());
    // 获取用户自定义告警会员
    String currentSpecialAccounts =
        Optional.ofNullable(user.getSettings())
            .map(JSONObject::parseObject)
            .map(s -> s.get("specialMemberWarn"))
            .map(Object::toString)
            .orElse("");
    // 公共告警会员
    SysDictData dictData =
        dictDataService.getDictData(DictTypeEnum.PUBLIC_WARNING_ACCOUNT, SysDictData.class);
    // 合并公共告警会员和自定义告警会员
    String specialAccounts =
        Optional.ofNullable(dictData)
            .map(SysDictData::getDictValue)
            .filter(StringUtils::isNotBlank)
            .map(c -> c + "," + currentSpecialAccounts)
            .orElse(currentSpecialAccounts);
    userDTO.setSpecialAccounts(specialAccounts);
    return adminCache.getOnlineList(page, userDTO);
  }

  /**
   * 踢用户下线
   *
   * @param userDTO
   * @return
   */
  @SentinelResource(value = "kickUser")
  public void kickUser(OnlineUserDTO userDTO) {
    if (Objects.equals(SecurityUserHolder.getUsername(), userDTO.getUserName())) {
      throw new ServiceException("不能踢自己");
    }
    jwtTokenService.removeToken(userDTO.getUserName());
  }

  @SentinelResource(value = "kickAllUser")
  public void kickAllUser(String sign) {}
}
