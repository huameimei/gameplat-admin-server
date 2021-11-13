package com.gameplat.admin.model.domain.limit;

import lombok.Data;

import java.io.Serializable;

@Data
public class EditUserInfoLimit implements Serializable {

    /**
     * 真实姓名0表示可见，1表示必填，2表示隐藏
     */
    private Integer realName;

    /**
     * 电话0表示可见，1表示必填，2表示隐藏
     */
    private Integer cellphoneNumber;

    /**
     * 生日0表示可见，1表示必填，2表示隐藏
     */
    private Integer birthdate;

    /**
     * 邮箱0表示可见，1表示必填，2表示隐藏
     */
    private Integer email;

    /**
     * QQ0表示可见，1表示必填，2表示隐藏
     */
    private Integer qq;
    /**
     * 微信0表示可见，1表示必填，2表示隐藏
     */
    private Integer weChat;

    /**
     * 出款必填 1开启 0 关闭
     */
    private Integer outStyleRequired;
}
