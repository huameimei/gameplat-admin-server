package com.gameplat.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.gameplat.admin.model.domain.ActivityLobby;
import com.gameplat.admin.model.dto.ActivityLobbyAddDTO;
import com.gameplat.admin.model.dto.ActivityLobbyDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateDTO;
import com.gameplat.admin.model.dto.ActivityLobbyUpdateStatusDTO;
import com.gameplat.admin.model.vo.ActivityLobbyVO;
import java.util.List;

/**
 * 活动大厅业务查询
 */
public interface ActivityLobbyService {


    /**
     * 查询活动大厅列表
     *
     * @param page
     * @param activityLobbyDTO
     * @return
     */
    IPage<ActivityLobbyVO> findActivityLobbyList(PageDTO<ActivityLobby> page, ActivityLobbyDTO activityLobbyDTO);

    /**
     * 新增活动大厅
     *
     * @param activityLobbyAddDTO
     */
    void add(ActivityLobbyAddDTO activityLobbyAddDTO);

    /**
     * 更新活动大厅
     *
     * @param activityLobbyUpdateDTO
     */
    void update(ActivityLobbyUpdateDTO activityLobbyUpdateDTO);

    /**
     * 删除一个或者多个数据
     *
     * @param ids
     */
    void remove(String ids);

    /**
     * 根据id删除活动大厅
     *
     * @param ids
     */
    void deleteActivityLobby(String ids);

    /**
     * @param activityLobbyUpdateStatusDTO
     */
    void updateStatus(ActivityLobbyUpdateStatusDTO activityLobbyUpdateStatusDTO);

    /**
     * 查询未绑定的大厅活动列表
     *
     * @return
     */
    List<ActivityLobbyVO> findUnboundLobbyList();
}
