package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.dto.GameTypeQueryDTO;
import com.gameplat.admin.model.dto.OperGameTypeDTO;
import com.gameplat.admin.model.vo.GameTypeVO;
import com.gameplat.model.entity.game.GameType;

public interface GameTypeService extends IService<GameType> {

  IPage<GameTypeVO> selectGameTypeList(PageDTO<GameType> page, GameTypeQueryDTO dto);

  void updateGameType(OperGameTypeDTO dto);
}
