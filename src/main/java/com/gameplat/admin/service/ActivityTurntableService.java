package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gameplat.model.entity.activity.ActivityTurntable;

public interface ActivityTurntableService extends IService<ActivityTurntable> {
    IPage<ActivityTurntable> findActivityTurntableList(PageDTO<ActivityTurntable> page, ActivityTurntable dto);

    boolean addActivityTurntable(ActivityTurntable bean);

    void delete(String ids);
}
