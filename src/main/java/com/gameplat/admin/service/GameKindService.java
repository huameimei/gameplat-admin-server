package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GameKind;
import com.gameplat.admin.model.dto.GameKindQueryDTO;
import com.gameplat.admin.model.dto.OperGameKindDTO;
import com.gameplat.admin.model.vo.GameKindVO;

public interface GameKindService extends IService<GameKind> {

  IPage<GameKindVO> selectGameKindList(PageDTO<GameKind> page, GameKindQueryDTO dto);

  void updateGameKind(OperGameKindDTO operGameKindDTO);

  void updateEnable(OperGameKindDTO operGameKindDTO);

  void updateDemoEnable(OperGameKindDTO operGameKindDTO);
}
