package com.gameplat.admin.model.dto;

import lombok.Data;

/**
 * 快捷回复配置DTO
 * @author three
 */
@Data
public class QuickReplyDTO {

    /**
     * 编号
     */
    private Long id;
    /**
     * 回复信息
     */
    private String message;
    /**
     * 消息类型
     */
    private String messageType;
    /**
     * 回复信息模糊模糊匹配
     */
    private Integer messageFuzzy;
}
