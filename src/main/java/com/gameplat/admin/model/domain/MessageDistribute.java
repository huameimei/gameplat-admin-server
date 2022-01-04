package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息分发到会员列表
 *
 * @author: kenvin
 * @date: 2021/5/1 9:22
 * @desc:
 */
@Data
@TableName("message_distribute")
public class MessageDistribute implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 消息id
     */
    private Long messageId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 充值层级
     */
    private Integer rechargeLevel;

    /**
     * VIP等级
     */
    private Integer vipLevel;

    /**
     * 代理层级
     */
    private String agentLevel;

    /**
     * 阅读状态：0 未读  1 已读
     */
    private Integer readStatus;

    /**
     * 发送移除标识
     */
    private Integer sendRemoveFlag;

    /**
     * 接收移除标识
     */
    private Integer acceptRemoveFlag;

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
