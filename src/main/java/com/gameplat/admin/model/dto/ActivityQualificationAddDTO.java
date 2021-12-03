package com.gameplat.admin.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 活动资格新增DTO
 *
 * @Author: whh
 * @Date: 2020/8/26 17:03
 * @Description: 添加资格管理
 */
@Data
public class ActivityQualificationAddDTO implements Serializable {

    /**
     * 1 活动大厅  2 红包雨
     */
    private Integer type;

    /**
     * 用户名
     */
    private String username;

    /**
     * 活动大厅信息
     */
    private List<ActivityLobbyDTO> activityLobbyList;

    /**
     * 活动红包信息
     */
    private List<ActivityRedPacketDTO> activityRedPacketList;

}
