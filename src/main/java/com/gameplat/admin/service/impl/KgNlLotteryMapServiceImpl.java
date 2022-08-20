package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.KgNlLotteryMapMapper;
import com.gameplat.admin.service.KgNlLotteryMapService;
import com.gameplat.model.entity.game.KgNlLotteryMap;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author aBen
 * @date 2022/8/11 21:40
 * @desc
 */
@Service
public class KgNlLotteryMapServiceImpl extends ServiceImpl<KgNlLotteryMapMapper, KgNlLotteryMap>
  implements KgNlLotteryMapService {

  @Override
  public List<KgNlLotteryMap> findMapList() {
    return this.lambdaQuery().list();
  }

}
