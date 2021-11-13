package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.LimitInfo;
import com.gameplat.admin.model.dto.LimitInfoDTO;

public interface LimitInfoService extends IService<LimitInfo> {

  void insertLimitInfo(LimitInfoDTO limitInfoDTO);

  <T> T getLimitInfo(String name, Class<T> t);

  LimitInfo<?> getLimitInfo(String name);
}
