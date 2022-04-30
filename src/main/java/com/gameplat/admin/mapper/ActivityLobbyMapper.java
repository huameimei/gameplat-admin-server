package com.gameplat.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import com.gameplat.model.entity.activity.ActivityLobby;

/**
 * @author lyq @Description 数据层
 * @date 2020-08-14 14:50:01
 */
public interface ActivityLobbyMapper extends BaseMapper<ActivityLobby> {
    ActivityLobbyVO getActivityLobbyVOById(Long id);
}
