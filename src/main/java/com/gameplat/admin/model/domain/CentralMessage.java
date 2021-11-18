package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 中心推送消息表
 * @date 2021/11/17
 */

@Data
@TableName("central_message")
public class CentralMessage implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 唯一ID
     */
    private String uid;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 启用状态：启用<0> | 禁用<1>
     */
    private Integer status;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 平台编码
     */
    private String platCode;

    /**
     * 接收人
     */
    private String receiverAccount;

    /**
     * 操作人的账号
     */
    private String operatorAccount;

    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date effectTime;

    /**
     * 失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /**
     * 消息版本号
     */
    private Long rowVersion;

    /**
     * 删除标记,1:删除
     */
    private String deleted;


}
