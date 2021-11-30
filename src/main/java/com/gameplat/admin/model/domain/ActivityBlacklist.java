package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @ApiModelProperty(value = "限制内容")
    private String limitedContent;

    @ApiModelProperty(value = "限制类型 1会员账号  2 ip地址")
    private Integer limitedType;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private String createTime;


}
