package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.LimitInfo;
import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.common.enums.LimitEnums;

public interface LimitInfoService extends IService<LimitInfo> {

  void insertLimitInfo(LimitInfoDTO limitInfoDTO);

  <T> T getLimitInfo(LimitEnums limit, Class<T> t);

  LimitInfo<?> getLimitInfo(String name);
}
