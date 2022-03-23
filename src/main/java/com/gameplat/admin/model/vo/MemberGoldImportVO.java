package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberGoldImportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String account;

    private Integer count;

}
