package com.gameplat.admin.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.GameBarConvert;
import com.gameplat.admin.mapper.GameBarMapper;
import com.gameplat.admin.model.dto.GameBarDTO;
import com.gameplat.admin.model.vo.GameBarVO;
import com.gameplat.admin.service.GameBarService;
import com.gameplat.model.entity.game.GameBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameBarServiceImpl extends ServiceImpl<GameBarMapper, GameBar> implements GameBarService {

  @Autowired private GameBarConvert convert;

  @Autowired private GameBarMapper mapper;

  private static final Integer BAR_TYPE = 0; //0 顶级导航

  /**
   * 获取游戏导航列表
   */
  @Override
  @SentinelResource(value = "gameBarList", fallback = "sentineFallBack")
  public List<GameBarVO> gameBarList(GameBarDTO dto) {
    List<GameBar> list = this.lambdaQuery().list();
    if (list != null && list.size() > 0){
      List<GameBarVO> barList = new ArrayList<>();
      list.forEach(x->{
        if (x.getGameType().equals(BAR_TYPE)){
          barList.add(convert.toVo(x));
        }
      });

      barList.forEach(x->{
        list.forEach(y->{
          if (!y.getGameType().equals(BAR_TYPE) && y.getCode().equals(x.getCode())){
            x.getGameBarList().add(y);
          }
        });
      });

      return barList;
    }
    return null;
  }

  /**
   * 编辑游戏导航配置
   */
  @Override
  @SentinelResource(value = "editGameBar", fallback = "sentineFallBack")
  public boolean editGameBar(GameBarDTO dto) {
    return false;
  }
}
