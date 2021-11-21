package com.gameplat.admin.model.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: whh
 * @Date: 2020/8/26 17:03
 * @Description: 添加资格管理
 */
@Data
public class ActivityQualificationAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 1 活动大厅  2 红包雨
     */
    private Integer type;

    private String username;

    private List<ActivityLobbyDiscountDTO> memberActivityLobbyDTO;

    private List<ActivityRedPacketDTO> memberRedPacketDTO;

}
