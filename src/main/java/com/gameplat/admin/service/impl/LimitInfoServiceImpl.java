package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.enums.LimitEnums;
import com.gameplat.admin.mapper.LimitInfoMapper;
import com.gameplat.admin.model.domain.LimitInfo;
import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.admin.service.LimitInfoService;
import com.gameplat.common.exception.ServiceException;
import com.gameplat.common.json.JsonUtils;
import com.gameplat.common.security.SecurityUserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/** 全局限制配置操作 */
@Slf4j
@Service
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
      limit.setAddTime(new Date());
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
  @SuppressWarnings("unchecked")
  public <T> T getLimitInfo(String name, Class<T> t) {
    LimitInfo<?> info = lambdaQuery().eq(LimitInfo::getName, name).one();
    if (info != null && info.getLimit() != null) {
      return (T) info.getLimit();
    }
    return null;
  }
}