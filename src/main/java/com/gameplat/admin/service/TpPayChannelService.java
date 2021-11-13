package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.TpPayChannel;
import com.gameplat.admin.model.dto.TpPayChannelAddDTO;
import com.gameplat.admin.model.dto.TpPayChannelEditDTO;
import com.gameplat.admin.model.dto.TpPayChannelQueryDTO;
import com.gameplat.admin.model.vo.TpPayChannelVO;

public interface TpPayChannelService extends IService<TpPayChannel> {

  void deleteByMerchantId(Long merchantId);

  void update(TpPayChannelEditDTO dto);

  void updateStatus(Long id, Integer status);

  void save(TpPayChannelAddDTO dto);

  void delete(Long id);

  IPage<TpPayChannelVO> findTpPayChannelPage(Page<TpPayChannel> page, TpPayChannelQueryDTO dto);
}
