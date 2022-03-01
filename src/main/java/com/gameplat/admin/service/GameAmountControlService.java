package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.GameAmountControlVO;
import com.gameplat.model.entity.game.GameAmountControl;

import java.util.List;

public interface GameAmountControlService extends IService<GameAmountControl> {

  List<GameAmountControlVO> selectGameAmountList();

  GameAmountControl findInfoByType(Integer type);
}
