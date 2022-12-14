package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.PayAccountAddDTO;
import com.gameplat.admin.model.dto.PayAccountEditDTO;
import com.gameplat.admin.model.dto.PayAccountQueryDTO;
import com.gameplat.admin.model.vo.PayAccountVO;
import com.gameplat.model.entity.pay.PayAccount;

import java.util.List;
import java.util.Map;

public interface PayAccountService extends IService<PayAccount> {

  void deleteByPayType(String payType);

  void update(PayAccountEditDTO dto);

  void updateStatus(Long id, Integer status);

  void save(PayAccountAddDTO dto);

  void delete(Long id);

  IPage<PayAccountVO> findPayAccountPage(Page<PayAccount> page, PayAccountQueryDTO dto);

  List<Map<String,String>> queryAccounts();
}
