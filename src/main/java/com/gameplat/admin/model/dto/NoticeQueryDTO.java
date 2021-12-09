package com.gameplat.admin.model.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author lily
 * @description 公告消息入参
 * @date 2021/11/16
 */

@Data
public class NoticeQueryDTO implements Serializable {

    /**
     * 公告标题
     */
    private String noticeTitle;

    /**
     * 类型 (1滚动公告 2登录公告 3推广公告 4注册公告 5彩票公告 6体育公告)
     */
    private Integer noticeType;

    /**
     * 启用状态 (0启用 1禁用)
     */
    private Integer status;
}
