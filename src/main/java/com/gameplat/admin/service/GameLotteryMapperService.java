package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.GameLottery;
import com.gameplat.admin.model.vo.GameLotteryVo;

import java.util.List;


public interface GameLotteryMapperService extends IService<GameLottery> {

  List<GameLotteryVo> findGameLotteryType(int code);

}
