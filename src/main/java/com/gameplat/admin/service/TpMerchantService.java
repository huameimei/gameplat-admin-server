package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.TpMerchant;
import com.gameplat.admin.model.dto.TpMerchantAddDTO;
import com.gameplat.admin.model.dto.TpMerchantEditDTO;
import com.gameplat.admin.model.vo.TpMerchantPayTypeVO;
import com.gameplat.admin.model.vo.TpMerchantVO;
import java.util.List;

public interface TpMerchantService extends IService<TpMerchant> {

  void update(TpMerchantEditDTO dto);

  void save(TpMerchantAddDTO dto);

  void delete(Long id);

  void updateStatus(Long id, Integer status);

  TpMerchantPayTypeVO getTpMerchantById(Long id);

  IPage<TpMerchantVO> queryPage(Page<TpMerchant> page, Integer status, String name);

  List<TpMerchantVO> queryAllMerchant(Integer status);
}
