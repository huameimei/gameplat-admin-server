package com.gameplat.admin.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_pay_bank")
public class SysPayBank extends BaseEntity<SysPayBank> {

    private String bankName;

    private String bankCode;

    private Integer sort;

    private Integer status;

    private Integer bankType;

    private String img;
}
