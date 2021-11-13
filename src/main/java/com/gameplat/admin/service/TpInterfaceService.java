package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.TpInterface;
import com.gameplat.admin.model.vo.TpInterfacePayTypeVo;
import com.gameplat.admin.model.vo.TpInterfaceVO;

import java.util.List;

public interface TpInterfaceService extends IService<TpInterface> {

  List<TpInterfaceVO> queryAll();

  TpInterfaceVO queryTpInterface(String interfaceCode);

  TpInterfacePayTypeVo queryTpInterfacePayType(Long id);

  void update(TpInterface tpInterface);

  void add(TpInterface tpInterface);

  void synchronization(TpInterface tpInterface);
}
