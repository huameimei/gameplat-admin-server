package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.entity.PpInterface;
import com.gameplat.admin.model.vo.PpInterfaceVO;
import java.util.List;

public interface PpInterfaceService extends IService<PpInterface> {

  List<PpInterfaceVO> queryAll();

  PpInterfaceVO queryPpInterface(String interfaceCode);
}
