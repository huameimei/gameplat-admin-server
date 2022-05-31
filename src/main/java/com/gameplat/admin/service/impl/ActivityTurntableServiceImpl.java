package com.gameplat.admin.service.impl;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gameplat.admin.mapper.ActivityTurntableMapper;
import com.gameplat.admin.service.ActivityTurntableService;
import com.gameplat.base.common.util.DateUtil;
import com.gameplat.base.common.util.StringUtils;
import com.gameplat.model.entity.activity.ActivityTurntable;
import com.gameplat.model.entity.activity.ActivityTurntablePrize;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author admin
 */
@Service
public class ActivityTurntableServiceImpl
    extends ServiceImpl<ActivityTurntableMapper, ActivityTurntable>
    implements ActivityTurntableService {
    @Override
    public IPage<ActivityTurntable> findActivityTurntableList(PageDTO<ActivityTurntable> page, ActivityTurntable dto,String startTime,String endTime) {
        return this.lambdaQuery()
                .like(dto!=null && StringUtils.isNotEmpty(dto.getTitle()),ActivityTurntable::getTitle,dto.getTitle())
                .ge(StringUtils.isNotEmpty(startTime), ActivityTurntable::getBeginTime,DateUtil.strToDate(startTime,DateUtil.YYYY_MM_DD_HH_MM_SS))
                .le(StringUtils.isNotEmpty(endTime),ActivityTurntable::getEndTime,DateUtil.strToDate(endTime,DateUtil.YYYY_MM_DD_HH_MM_SS))
                .eq(dto!=null && dto.getStatus()!=null,ActivityTurntable::getStatus,dto.getStatus())
                .page(page);
    }

    @Override
    public boolean addActivityTurntable(ActivityTurntable bean) {
        if(bean==null){
            return false;
        }
        if(StringUtils.isEmpty(bean.getPrizeClass())){
            bean.setPrizeClass(ActivityTurntablePrize.class.getName());
        }
        return this.saveOrUpdate(bean);
    }

    @Override
    public void delete(String ids) {
        if(StringUtils.isEmpty(ids)){
            throw new ServiceException("Id为空");
        }
        this.removeByIds(Arrays.asList(ids.split(",")));
    }
}
