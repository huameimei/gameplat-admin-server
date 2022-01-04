package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 站内信
 *
 * @author: kenvin
 * @date: 2021/4/28 15:53
 * @desc:
 */
@Data
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息标题
     */
    private String messageTitle;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * PC端图片
     */
    private String pcImage;

    /**
     * APP端图片
     */
    private String appImage;

    /**
     * 推送范围
     */
    private Integer pushRange;

    /**
     * 是否弹窗: 0 否  1 是
     */
    private Integer popupsFlag;

    /**
     * 弹出次数: 1 一次  2 多次
     */
    private Integer popupsFrequency;

    /**
     * 消息类型
     */
    private Integer messageType;

    /**
     * 弹窗类型
     */
    private String popupsType;

    /**
     * 是否即时消息: 0 否  1 是
     */
    private Integer immediateFlag;

    /**
     * 状态：0 禁用 1 启用
     */
    private Integer status;

    /**
     * 升序排序
     */
    private Integer sort;

    /**
     * 关联账号
     */
    private String linkAccount;

    /**
     * 会员层级
     */
    private String level;

    /**
     * 语种
     */
    private String language;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
