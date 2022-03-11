package com.gameplat.admin.service.impl;

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

import java.util.List;


@Service
@Transactional(isolation = Isolation.DEFAULT, rollbackFor = Throwable.class)
public class GameBarServiceImpl extends ServiceImpl<GameBarMapper, GameBar> implements GameBarService {

  @Autowired private GameBarConvert convert;

  @Autowired private GameBarMapper mapper;

  /**
   * 获取游戏导航列表
   */
  @Override
  public List<GameBarVO> gameBarList(GameBarDTO dto) {
    return null;
  }

  /**
   * 编辑游戏导航配置
   */
  @Override
  public boolean editGameBar(GameBarDTO dto) {
    return false;
  }
}
