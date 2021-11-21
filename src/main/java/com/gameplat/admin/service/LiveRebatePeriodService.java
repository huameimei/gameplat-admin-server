package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.admin.model.domain.LiveRebatePeriod;
import com.gameplat.admin.model.dto.LiveRebatePeriodQueryDTO;
import com.gameplat.admin.model.dto.OperLiveRebatePeriodDTO;
import com.gameplat.admin.model.vo.LiveRebatePeriodVO;

public interface LiveRebatePeriodService extends IService<LiveRebatePeriod> {

  IPage<LiveRebatePeriodVO> queryLiveRebatePeriod(Page<LiveRebatePeriod> page, LiveRebatePeriodQueryDTO dto);

  void addLiveRebatePeriod(OperLiveRebatePeriodDTO dto);

  void updateLiveRebatePeriod(OperLiveRebatePeriodDTO dto);

  void deleteLiveRebatePeriod(Long id,boolean only);

  void settle(Long id);
}
