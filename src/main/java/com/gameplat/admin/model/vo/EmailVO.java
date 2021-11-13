package com.gameplat.admin.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 邮件VO
 * @author three
 */
@Data
public class EmailVO {

    private Long id;
    private String title;
    private String collectAddress;
    private String sendAddress;
    private String content;
    private Integer emailType;
    private Integer status;
    private Date createTime;
}
