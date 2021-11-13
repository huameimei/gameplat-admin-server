package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.PpMerchant;
import com.gameplat.admin.model.dto.PpMerchantAddDTO;
import com.gameplat.admin.model.dto.PpMerchantEditDTO;
import com.gameplat.admin.model.vo.PpMerchantVO;

import java.util.List;

public interface PpMerchantService extends IService<PpMerchant> {

  void update(PpMerchantEditDTO dto);

  void save(PpMerchantAddDTO dto);

  void delete(Long id);

  void updateStatus(Long id, Integer status);

  PpMerchantVO getPpMerchantById(Long id);

  IPage<PpMerchantVO> queryPage(Page<PpMerchant> page, Integer status, String name);

  List<PpMerchantVO> queryAllMerchant(Integer status);
}
