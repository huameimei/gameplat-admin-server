package com.gameplat.admin.model.vo;

import lombok.Data;

/**
 * @Description : 代理信息接收
 * @Author : cc
 * @Date : 2022/7/20
 */
@Data
public class AgentInfoVo {
    private Long parentId;
    private String parentName;
    private String agentPath;
    private String account;
}
