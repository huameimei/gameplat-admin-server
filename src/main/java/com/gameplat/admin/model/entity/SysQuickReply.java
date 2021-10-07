package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Lenovo
 */
@Data
@TableName("sys_quick_reply")
public class SysQuickReply implements Serializable {

    private Long id;

    /**
     * 回复信息
     */
    private String message;

    /**
     * 回复信息类型 1-入款 2-出款
     */
    private Integer messageType;

    /**
     * 状态 关闭0、开启1
     */
    private Integer messageStatus;

}
