package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.LimitInfo;
import com.gameplat.admin.model.dto.LimitInfoDTO;
import com.gameplat.common.enums.LimitEnums;
import com.gameplat.common.model.bean.limit.AdminLoginLimit;
import com.gameplat.common.model.bean.limit.MemberRechargeLimit;

import java.util.Optional;

public interface LimitInfoService extends IService<LimitInfo> {

  void insertLimitInfo(LimitInfoDTO limitInfoDTO);

  <T> Optional<T> getLimitInfo(LimitEnums limit, Class<T> t);

  AdminLoginLimit getAdminLimit();

  MemberRechargeLimit getRechargeLimit();

  LimitInfo<?> getLimitInfo(String name);

  <T> T get(LimitEnums limit);

}
