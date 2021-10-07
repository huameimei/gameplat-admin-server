package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.entity.TpInterface;
import com.gameplat.admin.model.vo.TpInterfaceVO;

import java.util.List;

public interface TpInterfaceService extends IService<TpInterface> {

    List<TpInterfaceVO> queryAll();

    TpInterfaceVO queryTpInterface(String interfaceCode);
}
