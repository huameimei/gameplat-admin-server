package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;


/**
 * 活动黑名单
 *
 * @author kenvin
 * @Description 实体层
 * @date 2020-08-20 11:30:39
 */
@Data
@TableName("activity_blacklist")
public class ActivityBlacklist implements Serializable {

    private static final long serialVersionUID = 8368306176937351503L;
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 限制内容
     */
    private String limitedContent;

    /**
     * 限制类型 1会员账号  2 ip地址
     */
    private Integer limitedType;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createTime;

}
