package com.gameplat.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.convert.ActivityConvert;
import com.gameplat.admin.mapper.ActivityMapper;
import com.gameplat.admin.model.domain.Activity;
import com.gameplat.admin.model.dto.ActivityDTO;
import com.gameplat.admin.model.vo.ActivityVO;
import com.gameplat.admin.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 活动业务类
 *
 * @author admin
 */
@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity>
    implements ActivityService {

  @Autowired private ActivityConvert activityConvert;

  @Override
  public IPage<ActivityVO> list(PageDTO<Activity> page, ActivityDTO activityDTO) {
    LambdaQueryChainWrapper<Activity> queryWrapper = this.lambdaQuery();
    //        queryWrapper.eq(StringUtils.isNotBlank());

    return queryWrapper.page(page).convert(activityConvert::toVo);
  }
}
