package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.vo.GameLotteryVo;
import com.gameplat.model.entity.game.GameLottery;

import java.util.List;

public interface GameLotteryMapperService extends IService<GameLottery> {

  List<GameLotteryVo> findGameLotteryType(int code);
}
