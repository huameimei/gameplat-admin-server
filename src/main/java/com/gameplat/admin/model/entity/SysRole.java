package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@TableName("sys_role")
public class SysRole extends BaseEntity {

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色类型
     */
    private Integer roleType;

}
