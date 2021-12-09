package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.enums.DictTypeEnum;
import com.gameplat.admin.model.bean.OnlineCount;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.domain.SysUser;
import com.gameplat.admin.model.dto.OnlineUserDTO;
import com.gameplat.admin.model.vo.OnlineUserVo;
import com.gameplat.admin.service.OnlineUserService;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.base.common.util.CollectorUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import com.gameplat.security.service.JwtTokenService;
import eu.bitwalker.useragentutils.OperatingSystem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OnlineUserServiceImpl implements OnlineUserService {

  private static final String TOKEN_PREFIX = "token:*:*";

  @Autowired private SysUserService userService;

  @Autowired private SysDictDataService dictDataService;

  @Autowired private JwtTokenService jwtTokenService;

  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Override
  public List<UserCredential> getOnlineUsers() {
    return this.getOnlineUsers(this.getOnlineUserKeys(), null);
  }

  @Override
  @SentinelResource(value = "selectOnlineList")
  public PageExt<OnlineUserVo, OnlineCount> selectOnlineList(
      PageDTO<OnlineUserVo> page, OnlineUserDTO dto) {
    SysUser user = userService.getByUsername(SecurityUserHolder.getUsername());
    // 获取用户自定义告警会员
    List<String> warningUsers =
        Optional.ofNullable(user.getSettings())
            .map(JSONObject::parseObject)
            .map(c -> c.getString("specialMemberWarn"))
            .map(c -> StringUtils.split(c, ","))
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

    // 合并公共告警会员和自定义告警会员
    List<String> specialAccounts =
        dictDataService.getDictData(DictTypeEnum.PUBLIC_WARNING_ACCOUNT, ",");

    Set<String> warningAccounts = new HashSet<>(warningUsers);
    warningAccounts.addAll(specialAccounts);

    int total = 0;
    int current = (int) page.getCurrent();
    int size = (int) page.getSize();

    List<UserCredential> onlineUsers = new ArrayList<>(0);
    OnlineCount onlineCount = null;
    Set<String> keys = this.getOnlineUserKeys();

    if (CollectionUtil.isNotEmpty(keys)) {
      total = keys.size();
      onlineUsers = this.getOnlineUsers(keys, dto);
      // 在线会员统计
      onlineCount = this.countOnline(onlineUsers, warningAccounts);
      this.filterOnlineUserByCondition(onlineUsers, dto);
    }

    // 分页
    List<OnlineUserVo> records =
        CollectorUtils.page(onlineUsers, current, size).stream()
            .map(this::convert)
            .collect(Collectors.toList());

    return PageExt.of(current, size, total, records, onlineCount);
  }

  @Override
  @SentinelResource(value = "kick")
  public void kick(String username) {
    jwtTokenService.removeToken(username);
  }

  @Override
  @SentinelResource(value = "kickAll")
  public void kickAll() {
    Set<String> keys = redisTemplate.keys(TOKEN_PREFIX + "*");
    if (CollectionUtil.isNotEmpty(keys)) {
      redisTemplate.delete(keys);
    }
  }

  private OnlineCount countOnline(List<UserCredential> onlineUsers, Set<String> warningAccounts) {
    OnlineCount onlineCount = new OnlineCount();
    onlineUsers.forEach(
        user -> {
          String userType = user.getUserType();
          String clientType = user.getClientType();

          // 统计客户端类型
          if (clientType.contains(OperatingSystem.IOS.getName())) {
            onlineCount.setIphoneCount(onlineCount.getIphoneCount() + 1);
          } else if (clientType.contains(OperatingSystem.ANDROID.getName())) {
            onlineCount.setAndroidCount(onlineCount.getAndroidCount() + 1);
          } else if (clientType.contains(OperatingSystem.WINDOWS.getName())) {
            onlineCount.setWindowsCount(onlineCount.getWindowsCount() + 1);
          } else if (clientType.contains(OperatingSystem.MAC_OS.getName())) {
            onlineCount.setWindowsCount(onlineCount.getWindowsCount() + 1);
          }

          // 统计会员数量
          if (UserTypes.MEMBER.match(userType)) {
            onlineCount.setMemberCount(onlineCount.getMemberCount() + 1);
          } else if (UserTypes.TEST.match(userType)) {
            onlineCount.setTestUserCount(onlineCount.getTestUserCount() + 1);
          } else if (warningAccounts.contains(user.getUsername())) {
            onlineCount.setWarningCount(onlineCount.getWarningCount() + 1);
          }
        });

    return onlineCount;
  }

  private Set<String> getOnlineUserKeys() {
    return redisTemplate.keys(TOKEN_PREFIX);
  }

  private OnlineUserVo convert(UserCredential credential) {
    return OnlineUserVo.builder()
        .username(credential.getUsername())
        .nickname(credential.getNickname())
        .realName(credential.getRealName())
        .userType(credential.getUserType())
        .loginIp(credential.getLoginIp())
        .lastLoginDate(credential.getLoginDate())
        .deviceType(credential.getDeviceType())
        .clientType(credential.getClientType())
        .build();
  }

  private List<UserCredential> getOnlineUsers(Set<String> keys, OnlineUserDTO dto) {
    return Optional.ofNullable(redisTemplate.opsForValue().multiGet(keys))
        .map(e -> e.stream().map(c -> (UserCredential) c).collect(Collectors.toList()))
        .orElseGet(Collections::emptyList);
  }

  /**
   * 按条件过滤在线会员
   *
   * @param credentials List
   * @param dto OnlineUserDTO
   */
  private void filterOnlineUserByCondition(List<UserCredential> credentials, OnlineUserDTO dto) {
    String username = dto.getUserName();
    if (StringUtils.isNotEmpty(username)) {
      credentials.removeIf(e -> !username.equals(e.getUsername()));
    }

    String userType = dto.getUserType();
    if (StringUtils.isNotEmpty(userType)) {
      if (UserTypes.WARN.match(userType) && !UserTypes.isAdminUser(userType)) {
        // 如果是告警会员则匹配用户名
        credentials.removeIf(e -> !dto.getWaningAccounts().contains(e.getUsername()));
      } else {
        credentials.removeIf(e -> !userType.equals(e.getUserType()));
      }
    }
  }
}
