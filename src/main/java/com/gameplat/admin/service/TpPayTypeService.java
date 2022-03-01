package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.TpPayTypeVO;
import com.gameplat.model.entity.pay.TpPayType;

import java.util.List;

public interface TpPayTypeService extends IService<TpPayType> {

  List<TpPayTypeVO> queryTpPayTypes(String tpInterfaceCode);

  void deleteBatchIds(List<Long> ids);
}
