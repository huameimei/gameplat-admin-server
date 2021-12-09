package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author lily
 * @description 新增公告消息入参
 * @date 2021/11/16
 */

@Data
public class NoticeUpdateStatusDTO implements Serializable {

    private Integer id;

    /**
     * 操作人的账号
     */
    private String operator;

    /**
     * 启用状态 (0.启用、1.禁用)
     */
    private Integer status;

    /**
     * 修改时间
     */
    private Date updateTime;
}
