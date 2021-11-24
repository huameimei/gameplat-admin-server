package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.LimitInfoMapper;
import com.gameplat.admin.model.domain.LimitInfo;
import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.json.JsonUtils;
import com.gameplat.security.SecurityUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/** 全局限制配置操作 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class LimitInfoServiceImpl extends ServiceImpl<LimitInfoMapper, LimitInfo>
    implements LimitInfoService {

  @Autowired private LimitInfoMapper limitInfoMapper;

  @Override
  public void insertLimitInfo(LimitInfoDTO limitInfoDTO) {
    String username = SecurityUserHolder.getUsername();
    Object obj =
        JsonUtils.parse(
            JsonUtils.toJson(limitInfoDTO.getParams()),
            LimitEnums.getClass(limitInfoDTO.getName()));
    LimitInfo info = lambdaQuery().eq(LimitInfo::getName, limitInfoDTO.getName()).one();
    int result = 0;
    if (info != null) {
      info.setUpdateTime(new Date());
      info.setLimit(obj);
      info.setValue(JsonUtils.toJson(obj));
      info.setOperator(username);
      LambdaUpdateWrapper<LimitInfo> lambdaUpdateWrapper = new LambdaUpdateWrapper();
      lambdaUpdateWrapper.eq(LimitInfo::getName, info.getName());
      result = this.limitInfoMapper.update(info, lambdaUpdateWrapper);
    } else {
      LimitInfo<Object> limit = new LimitInfo<>();
      limit.setName(limitInfoDTO.getName());
      limit.setOperator(username);
      limit.setLimit(obj);
      limit.setValue(JsonUtils.toJson(obj));
      limit.setValueClass(LimitEnums.getClass(limitInfoDTO.getName()).getName());
      result = this.limitInfoMapper.insert(limit);
    }
    if (result <= 0) {
      throw new ServiceException("添加失败!");
    }
  }

  @Override
  public LimitInfo<?> getLimitInfo(String name) {
    return this.lambdaQuery().eq(LimitInfo::getName, name).one();
  }

  @Override
  public <T> T getLimitInfo(LimitEnums limit, Class<T> t) {
    return lambdaQuery()
        .eq(LimitInfo::getName, limit.name())
        .oneOpt()
        .map(LimitInfo::getValue)
        .map(v -> JsonUtils.parse(v, t))
        .orElse(null);
  }
}
