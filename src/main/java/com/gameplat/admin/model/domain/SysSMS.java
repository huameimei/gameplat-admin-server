package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 短信
 * @author three
 */
@Data
@TableName("sys_sms")
public class SysSMS implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer operator;

    private String operatorName;

    private Integer smsType;

    private String phone;

    private String validCode;

    private String content;

    private String requestIp;

    private Date expireDate;

    private Integer status;

    private String message;

    private Date createTime;

    private Date updateTime;
}
