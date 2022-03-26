package com.gameplat.admin.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lily
 * @description
 * @date 2022/3/26
 */

@Data
public class TestVO implements Serializable {

    private Long memberId;
    private String account;
    private Long changes;
    private Integer type;
    private Integer vip;
    private Long growth;
    private String remark;
}
