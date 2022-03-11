package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.LimitInfoMapper;
import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.base.common.exception.ServiceException;
import com.gameplat.base.common.json.JsonUtils;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.lang.Assert;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import com.gameplat.common.model.bean.limit.MemberRechargeLimit;
import com.gameplat.model.entity.limit.LimitInfo;
import com.gameplat.security.SecurityUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/** 全局限制配置操作 */
@Slf4j
@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class LimitInfoServiceImpl extends ServiceImpl<LimitInfoMapper, LimitInfo>
    implements LimitInfoService {

  @Override
  public void insertLimitInfo(LimitInfoDTO limitInfoDTO) {
    String username = SecurityUserHolder.getUsername();
    Object obj =
        JsonUtils.parse(
            JsonUtils.toJson(limitInfoDTO.getParams()),
            LimitEnums.getClass(limitInfoDTO.getName()));

    LimitInfo info = lambdaQuery().eq(LimitInfo::getName, limitInfoDTO.getName()).one();
    if (info != null) {
      info.setUpdateTime(new Date());
      info.setValue(JsonUtils.toJson(obj));
      info.setOperator(username);
      LambdaUpdateWrapper<LimitInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
      lambdaUpdateWrapper.eq(LimitInfo::getName, info.getName());
      Assert.isTrue(this.update(info, lambdaUpdateWrapper), "修改失败!");
    } else {
      LimitInfo limit = new LimitInfo();
      limit.setName(limitInfoDTO.getName());
      limit.setOperator(username);
      limit.setValue(JsonUtils.toJson(obj));
      limit.setValueClass(LimitEnums.getClass(limitInfoDTO.getName()).getName());
      Assert.isTrue(this.save(limit), "添加失败!");
    }
  }

  @Override
  public LimitInfo getLimitInfo(String name) {
    return this.lambdaQuery().eq(LimitInfo::getName, name).one();
  }

  @Override
  public <T> Optional<T> getLimitInfo(LimitEnums limit, Class<T> t) {
    return lambdaQuery()
        .select(LimitInfo::getValue)
        .eq(LimitInfo::getName, limit.getName())
        .oneOpt()
        .map(LimitInfo::getValue)
        .map(v -> JsonUtils.parse(v, t));
  }

  @Override
  public AdminLoginLimit getAdminLimit() {
    return this.getLimitInfo(LimitEnums.ADMIN_LOGIN_CONFIG, AdminLoginLimit.class)
        .orElseThrow(() -> new ServiceException("登录配置信息不存在!"));
  }

  @Override
  public MemberRechargeLimit getRechargeLimit() {
    return this.getLimitInfo(LimitEnums.MEMBER_RECHARGE_LIMIT, MemberRechargeLimit.class)
        .orElseThrow(() -> new ServiceException("加载出入款配置信息失败，请联系客服!"));
  }

  @Override
  public <T> T get(LimitEnums limit) {
    return (T)
        this.lambdaQuery()
            .eq(LimitInfo::getName, limit.getName())
            .oneOpt()
            .map(LimitInfo::getValue)
            .map(v -> JsonUtils.parse(v, limit.getValue()))
            .orElse(null);
  }
}
