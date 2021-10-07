package com.gameplat.admin.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SysRoleMenu implements Serializable {

    private Integer roleId;

    private Integer menuId;

    private Integer halfId;

}
