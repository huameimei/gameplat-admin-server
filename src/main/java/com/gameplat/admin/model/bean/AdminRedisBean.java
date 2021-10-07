package com.gameplat.admin.model.bean;

import com.gameplat.admin.model.entity.SysRole;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class AdminRedisBean implements Serializable {

    private Long adminId;

    private TokenInfo tokenInfo;

    private SysRole role;

    private List<String> menuList;

    private List<String> elementList;

    private List<String> filterList = new ArrayList<String>();

}