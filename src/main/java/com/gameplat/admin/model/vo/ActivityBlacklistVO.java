package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;


/**
 * 活动黑名单VO
 * @author aBen
 * @Description 实体层
 * @date 2020-08-20 11:30:39
 */
@Data
public class ActivityBlacklistVO implements Serializable {


    private static final long serialVersionUID = 7535872776555957753L;
    @ApiModelProperty(value = "主键ID")
	private Long id;

    @ApiModelProperty(value = "活动ID")
    private Long activityId;

    @ApiModelProperty(value =   "限制内容")
    private String limitedContent;

    @ApiModelProperty(value = "限制类型 1会员账号  2 ip地址")
    private Integer limitedType;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createTime;


}
