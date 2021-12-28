package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 会员在线状态变更表
 * @date 2021/12/28
 */

@Data
@TableName("member_status")
public class MemberStatus implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 会员id */
    private Integer memberId;

    /** 登录客户端类型: 1 安卓，2 ios,3 pc */
    private Integer clientType;

    /** 登录ip */
    private String loginIp;

    /** 创建时间 */
    private Date createTime;

    /** 最近操作时间 */
    private Date lastOperateTime;

    /** 本次登录标识 */
    private String loginFlag;

    /** 1 移动端 2网页端 */
    private Integer loginType;

    /** 下线表示（0上线，1下线） */
    private Integer offline;

    /** 登录地址（国家省份城市） */
    private String loginAddress;

    /** 1、注册 2、登录 3、充值 4、提现 */
    private Integer type;

    private String loginDomain;
}
