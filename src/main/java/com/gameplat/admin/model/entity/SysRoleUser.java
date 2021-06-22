package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@TableName("sys_role_user")
public class SysRoleUser {
    private String userId;
    private String roleId;
}
