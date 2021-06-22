package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.cache.AdminCache;
import com.gameplat.admin.constant.Constants;
import com.gameplat.admin.dao.SysMenuMapper;
import com.gameplat.admin.dao.SysRoleMapper;
import com.gameplat.admin.dao.SysUserMapper;
import com.gameplat.admin.enums.AdminTypeEnum;
import com.gameplat.admin.enums.DictTypeEnum;
import com.gameplat.admin.enums.UserStateEnum;
import com.gameplat.admin.model.bean.AdminLoginLimit;
import com.gameplat.admin.model.bean.AdminRedisBean;
import com.gameplat.admin.model.bean.TokenInfo;
import com.gameplat.admin.model.entity.SysMenu;
import com.gameplat.admin.model.entity.SysRole;
import com.gameplat.admin.model.entity.SysUser;
import com.gameplat.admin.model.vo.UserEquipmentVO;
import com.gameplat.admin.service.SysDictDataService;
import com.gameplat.admin.service.SysUserService;
import com.gameplat.admin.utils.TokenManager;
import com.gameplat.common.exception.BusinessException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/** @author Lenovo */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {

  @Autowired private SysRoleMapper sysRoleMapper;

  @Autowired private SysMenuMapper sysMenuMapper;

  @Autowired private SysUserMapper sysUserMapper;

  @Autowired private SysDictDataService sysDictDataService;

  @Autowired private AdminCache adminCache;

  @Resource private RedisTemplate<String, Object> redisTemplate;

  @Override
  public SysUser getUserByAccount(String account) {
    return this.lambdaQuery().eq(SysUser::getAccount, account).one();
  }

  @Caching(
      put = {
        @CachePut(
            value = "tokenInfo",
            key = "'admin_' + #result.id",
            condition = "#result != null"),
        @CachePut(value = "online", key = "'admin_' + #result.id", condition = "#result != null")
      })
  @Override
  public TokenInfo login(
      String account,
      String password,
      String requestIp,
      UserEquipmentVO equipment,
      String userAgentString)
      throws Exception {
    SysUser admin = this.getUserByAccount(account);
    if (admin == null) {
      throw new BusinessException("账号不存在");
    }
    if (!UserStateEnum.isLogin(admin.getState())) {
      // 用户状态不对
      throw new BusinessException("账号已被停用");
    }
    AdminLoginLimit adminLoginLimit =
        sysDictDataService.getDictData(
            DictTypeEnum.ADMIN_LOGIN_CONFIG.getValue(), AdminLoginLimit.class);
    // 密码最大错误次数
    int num = adminLoginLimit.getErrorPwdLimit();

    TokenInfo tokenInfo = new TokenInfo();
    tokenInfo.setAccount(admin.getAccount());
    tokenInfo.setLoginDate(new Date());
    tokenInfo.setLoginIp(requestIp);
    tokenInfo.setUserType(admin.getUserType());
    tokenInfo.setExpiresIn(Constants.LOGIN_EXPIRE);
    // 线程记录用户登录日志

    String redisKey = String.format("tokenInfo_admin_%d", tokenInfo.getUid());
    if (redisTemplate.hasKey(redisKey)) {
      log.info("管理员uid={}，重新登录", tokenInfo.getUid());
    }
    return tokenInfo;
  }

  @Caching()
  @Override
  public void logout(Long adminId) {}

  public TokenInfo getTokenInfo(Long uid) {
    return adminCache.getAdminTokenInfo(uid);
  }

  @Cacheable(value = "privilege", key = "'admin_'+#id")
  @Override
  public AdminRedisBean getPrivilege(Long id) {
    log.info("------------getPrivilege--------------");
    SysUser sysUser = sysUserMapper.selectById(id);
    if (sysUser == null) {
      return null;
    }
    Long roleId = sysUser.getRoleId();
    SysRole sysRole = sysRoleMapper.selectById(roleId);
    List<String> list = new ArrayList<>();
    if (AdminTypeEnum.isSuperAdmin(sysUser.getUserType())) {
      list.add("*:*:*");
    } else {
      list = sysMenuMapper.selectMenuByRoleId(sysRole.getId());
    }
    AdminRedisBean adminRedisBean = new AdminRedisBean();
    adminRedisBean.setAdminId(id);
    adminRedisBean.setMenuList(list);
    adminRedisBean.setRole(sysRole);
    return adminRedisBean;
  }

  public void updateAdminTokenExpire(Long uid) {
    adminCache.updateAdminTokenExpire(uid);
  }
}
