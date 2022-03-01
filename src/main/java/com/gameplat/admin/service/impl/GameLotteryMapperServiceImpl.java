package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameLotteryConvert;
import com.gameplat.admin.mapper.GameLotteryMapper;
import com.gameplat.admin.model.domain.GameLottery;
import com.gameplat.admin.model.vo.GameLotteryVo;
import com.gameplat.admin.service.GameLotteryMapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameLotteryMapperServiceImpl
    extends ServiceImpl<GameLotteryMapper, GameLottery>
    implements GameLotteryMapperService {

    @Autowired(required = false)
    private GameLotteryConvert gameLotteryConvert;


  @Override
  public List<GameLotteryVo> findGameLotteryType(int code) {
     return this.lambdaQuery().eq(GameLottery::getLotteryType,code)
              .list().stream().map(gameLotteryConvert::toVo).collect(Collectors.toList());
  }
}
