package com.gameplat.admin.model.dto;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class MemberInfoEditDTO extends BaseEntity {

    /**
     * 会员账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 语种
     */
    private String language;

    /**
     * 手机号
     */
    private String phone;

    /**
     * QQ
     */
    private String qq;

    /**
     * 微信
     */
    private String wechat;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 用户性别（0男 1女 2未知）
     */
    private Integer sex;

    /**
     * 代理等级或会员层级
     */
    private Integer userLevel;

    /**
     * 会员状态
     */
    private Integer status;

    /**
     * 会员备注
     */
    private String remark;

    /**
     * 父级ID
     */
    private Long parentId;
}
