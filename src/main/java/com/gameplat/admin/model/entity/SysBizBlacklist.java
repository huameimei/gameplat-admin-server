package com.gameplat.admin.model.entity;

import com.gameplat.common.model.entity.BaseEntity;
import lombok.Data;

@Data
public class SysBizBlacklist extends BaseEntity {

    /**
     * 目标类型{0: 会员, 1: 层级}
     */
    private String targetType;

    /**
     * 目标
     */
    private String target;

    /**
     * 黑名单类型
     */
    private String types;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态{0: 禁用, 1: 启用}
     */
    private Integer status;
}
