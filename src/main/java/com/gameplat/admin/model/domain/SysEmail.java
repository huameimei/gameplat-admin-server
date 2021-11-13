package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 邮件
 * @author three
 */
@Data
@TableName("sys_email")
public class SysEmail implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String collectAddress;

    private String sendAddress;

    private String title;

    private String content;

    private Integer emailType;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private String remark;
}
