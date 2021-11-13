package com.gameplat.admin.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 快捷回复VO
 * @author three
 */
@Data
public class QuickReplyVO {

    private Long id;
    private String message;
    private Date createTime;
    private Date updateTime;
}
