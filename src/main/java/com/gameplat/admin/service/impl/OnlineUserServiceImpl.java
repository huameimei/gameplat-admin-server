package com.gameplat.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.bean.OnlineCount;
import com.gameplat.admin.model.bean.PageExt;
import com.gameplat.admin.model.dto.OnlineUserDTO;
import com.gameplat.admin.model.vo.MemberInfoVO;
import com.gameplat.admin.model.vo.OnlineUserVo;
import com.gameplat.admin.service.ConfigService;
import com.gameplat.admin.service.MemberService;
import com.gameplat.admin.service.OnlineUserService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.base.common.enums.ClientType;
import com.gameplat.base.common.util.CollectorUtils;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.common.enums.UserTypes;
import com.gameplat.model.entity.sys.SysUser;
import com.gameplat.security.SecurityUserHolder;
import com.gameplat.security.context.UserCredential;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.gameplat.common.enums.DictDataEnum.PUBLIC_WARNING_ACCOUNT;

@Slf4j
@Service
public class OnlineUserServiceImpl implements OnlineUserService {

  private static final String TOKEN_PREFIX = "token:*:*";

  private static final String MEMBER_TOKEN_PREFIX = "token:web:";

  @Autowired private SysUserService userService;

  @Autowired private ConfigService configService;

  @Autowired private MemberService memberService;

  @Autowired private RedisTemplate<String, Object> redisTemplate;

  @Override
  public List<UserCredential> getOnlineUsers() {
    return this.getOnlineUsers(this.getOnlineUserKeys());
  }

  @Override
  public Boolean isOnline(String account) {
    return redisTemplate.hasKey(MEMBER_TOKEN_PREFIX + account);
  }

  @Override
  @SentinelResource(value = "selectOnlineList")
  public PageExt<OnlineUserVo, OnlineCount> selectOnlineList(
      PageDTO<OnlineUserVo> page, OnlineUserDTO dto) {
    List<String> warningAccounts = this.getWarningAccounts();

    int total = 0;
    int current = (int) page.getCurrent();
    int size = (int) page.getSize();

    List<UserCredential> onlineUsers = new ArrayList<>(0);
    OnlineCount onlineCount = null;
    Set<String> keys = this.getOnlineUserKeys();

    if (CollectionUtil.isNotEmpty(keys)) {
      total = keys.size();
      onlineUsers = this.getOnlineUsers(keys);
      // 在线会员统计
      onlineCount = this.countOnline(onlineUsers, warningAccounts);
      onlineUsers = this.filterOnlineUserByCondition(onlineUsers, dto, warningAccounts);
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
  public void kick(String uuid) {
    Set<String> keys = this.getOnlineUserKeys();
    if (CollectionUtil.isNotEmpty(keys)) {
      MD5 md5 = MD5.create();
      keys.forEach(
          key -> {
            if (md5.digestHex(key).equals(uuid)) {
              redisTemplate.delete(key);
            }
          });
    }
  }

  @Override
  @SentinelResource(value = "kickAll")
  public void kickAll() {
    Set<String> keys = redisTemplate.keys(TOKEN_PREFIX + "*");
    if (CollectionUtil.isNotEmpty(keys)) {
      redisTemplate.delete(keys);
    }
  }

  private OnlineCount countOnline(List<UserCredential> onlineUsers, List<String> warningAccounts) {
    OnlineCount onlineCount = new OnlineCount();
    onlineUsers.forEach(
        user -> {
          // 统计客户端类型
          this.countClientType(onlineCount, user.getClientType());
          // 统计会员数量
          this.countUserType(onlineCount, warningAccounts, user);
        });

    return onlineCount;
  }

  private void countClientType(OnlineCount onlineCount, String clientType) {
    if (ClientType.IOS.match(clientType)) {
      onlineCount.setIosCount(onlineCount.getIosCount() + 1);
    } else if (ClientType.ANDROID.match(clientType)) {
      onlineCount.setAndroidCount(onlineCount.getAndroidCount() + 1);
    } else if (ClientType.H5.match(clientType)) {
      onlineCount.setH5Count(onlineCount.getH5Count() + 1);
    } else if (ClientType.WEB.match(clientType)) {
      onlineCount.setWindowsCount(onlineCount.getWindowsCount() + 1);
    } else {
      onlineCount.setOtherCount(onlineCount.getOtherCount() + 1);
    }
  }

  private void countUserType(
      OnlineCount onlineCount, List<String> warningAccounts, UserCredential credential) {
    String userType = credential.getUserType();
    if (UserTypes.MEMBER.match(userType)) {
      onlineCount.setMemberCount(onlineCount.getMemberCount() + 1);
    } else if (UserTypes.TEST.match(userType)) {
      onlineCount.setTestUserCount(onlineCount.getTestUserCount() + 1);
    } else if (warningAccounts.contains(credential.getUsername())) {
      onlineCount.setWarningCount(onlineCount.getWarningCount() + 1);
    }
  }

  private Set<String> getOnlineUserKeys() {
    return redisTemplate.keys(TOKEN_PREFIX);
  }

  private OnlineUserVo convert(UserCredential credential) {
    OnlineUserVo onlineUser =
        OnlineUserVo.builder()
            .uuid(credential.getUuid())
            .username(credential.getUsername())
            .userType(credential.getUserType())
            .loginIp(credential.getLoginIp())
            .lastLoginDate(credential.getLoginDate())
            .deviceType(credential.getDeviceType())
            .clientType(credential.getClientType())
            .build();

    if (UserTypes.isAdminUser(credential.getUserType())) {
      SysUser user = userService.getById(credential.getUserId());
      onlineUser.setNickname(user.getNickName());
    } else {
      // 查询会员信息
      MemberInfoVO memberInfo = memberService.getInfo(credential.getUserId());
      onlineUser.setParentName(memberInfo.getParentName());
      onlineUser.setRealName(memberInfo.getRealName());
      onlineUser.setBalance(memberInfo.getBalance());
      onlineUser.setNickname(memberInfo.getNickname());
    }

    return onlineUser;
  }

  private List<UserCredential> getOnlineUsers(Set<String> keys) {
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
  private List<UserCredential> filterOnlineUserByCondition(
      List<UserCredential> credentials, OnlineUserDTO dto, List<String> warningAccounts) {
    return credentials.stream()
        .filter(e -> this.filterByUsername(e, dto.getUserName()))
        .filter(e -> this.filterByUserType(e, dto.getUserType(), warningAccounts))
        .collect(Collectors.toList());
  }

  private boolean filterByUsername(UserCredential credential, String username) {
    return StringUtils.isBlank(username) || credential.getUsername().equals(username);
  }

  private boolean filterByUserType(
      UserCredential credential, String userType, List<String> warningAccounts) {
    if (StringUtils.isBlank(userType)) {
      return true;
    }

    if (UserTypes.WARN.match(userType)) {
      return warningAccounts.contains(credential.getUsername());
    } else {
      return StringUtils.equals(userType, credential.getUserType());
    }
  }

  /**
   * 获取告警会员，包含自定义和公共告警会员
   *
   * @return List<String>
   */
  private List<String> getWarningAccounts() {
    List<String> warningAccounts = new ArrayList<>();
    warningAccounts.addAll(this.getPublicWarningAccounts());
    warningAccounts.addAll(this.getCustomerWarningAccounts());
    return warningAccounts;
  }

  /**
   * 获取用户自定义告警会员
   *
   * @return List<String>
   */
  private List<String> getCustomerWarningAccounts() {
    SysUser user = userService.getByUsername(SecurityUserHolder.getUsername());
    return Optional.ofNullable(user.getSettings())
        .map(JSONObject::parseObject)
        .map(c -> c.getString("specialMemberWarn"))
        .map(c -> StringUtils.split(c, ","))
        .map(Arrays::asList)
        .orElse(Collections.emptyList());
  }

  /**
   * 获取公共告警会员
   *
   * @return List<String>
   */
  private List<String> getPublicWarningAccounts() {
    String dictDataList = configService.getValue(PUBLIC_WARNING_ACCOUNT);
    return Optional.ofNullable(dictDataList)
        .map(c -> StringUtils.split(c, ","))
        .map(Arrays::asList)
        .orElseGet(Collections::emptyList);
  }
}
