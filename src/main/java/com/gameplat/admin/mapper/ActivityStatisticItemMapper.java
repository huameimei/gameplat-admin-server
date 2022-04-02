package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.bean.ActivityStatisticItem;

import java.util.Map;

/**
 * @author aBen
 * @date 2021/12/13 19:58
 * @desc
 */
public interface ActivityStatisticItemMapper extends BaseMapper<ActivityStatisticItem> {

  ActivityStatisticItem findFirstRechargeOrder(Map map);

}
