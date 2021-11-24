package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.dto.ActivityLobbyAddDTO;
import com.gameplat.admin.model.dto.ActivityLobbyDTO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;

/**
 * 活动大厅业务查询
 */
public interface ActivityLobbyService {


    /**
     * 查询活动大厅列表
     * @param page
     * @param activityLobbyDTO
     * @return
     */
    IPage<ActivityLobbyVO> findActivityLobbyList(PageDTO<ActivityLobby> page, ActivityLobbyDTO activityLobbyDTO);

    /**
     * 新增
     * @param activityLobbyAddDTO
     */
    void add(ActivityLobbyAddDTO activityLobbyAddDTO);
}
