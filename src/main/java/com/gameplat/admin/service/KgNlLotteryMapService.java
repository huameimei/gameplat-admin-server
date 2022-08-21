package com.gameplat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.game.KgNlLotteryMap;

import java.util.List;

/**
 * @author aBen
 * @date 2022/8/11 21:38
 * @desc
 */
public interface KgNlLotteryMapService extends IService<KgNlLotteryMap> {

  List<KgNlLotteryMap> findMapList();
}
