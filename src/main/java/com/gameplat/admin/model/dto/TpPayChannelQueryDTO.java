package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TpPayChannelQueryDTO implements Serializable {

    private Long tpInterfaceId;

    private Integer status;

    private String payType;

    private List<String> userLevelList;
}
