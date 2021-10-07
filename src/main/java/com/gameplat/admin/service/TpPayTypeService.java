package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.entity.TpPayType;
import com.gameplat.admin.model.vo.TpPayTypeVO;

import java.util.List;

public interface TpPayTypeService extends IService<TpPayType> {

    List<TpPayTypeVO> queryTpPayTypes(String interfaceCode);
}
