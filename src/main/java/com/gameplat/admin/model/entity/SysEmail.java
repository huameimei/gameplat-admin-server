package com.gameplat.admin.model.entity;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class SysEmail extends BaseEntity<SysEmail> {

    /**
     * 收件人邮箱地址
     */
    private String collectAddress;

    /**
     * 发件人邮箱地址
     */
    private String sendAddress;

    /**
     * 邮件标题
     */
    private String title;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 发送类型 （1、qq 2、163）
     */
    private Integer type;

    /**
     * 发送状态（1、发送成功 2、发送失败 ）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
