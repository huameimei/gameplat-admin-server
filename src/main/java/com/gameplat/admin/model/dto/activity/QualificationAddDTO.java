package com.gameplat.admin.model.dto.activity;


import com.live.cloud.backend.model.activity.vo.MemberRedPacketVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: whh
 * @Date: 2020/8/26 17:03
 * @Description: 添加资格管理
 */
@Data
public class QualificationAddDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 1 活动大厅  2 红包雨
     */
    private Integer type;

    private String username;

    private List<com.live.cloud.backend.model.activity.dto.MemberActivityLobbyDTO> memberActivityLobbyDTO;

    private List<MemberRedPacketVO> memberRedPacketDTO;

}
