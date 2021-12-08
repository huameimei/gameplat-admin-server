package com.gameplat.admin.model.vo;

import java.util.Date;
import lombok.Data;

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
