package com.gameplat.admin.model.bean;

import lombok.Data;

import java.io.Serializable;


/**
 * @author: summer
 * @description: 配置类基类
 * @create: 2020/8/11 13:46
 **/
@Data
public class SportGameConfig implements Serializable {

    private String host;
    private String appId;
    private String appKey;

}
