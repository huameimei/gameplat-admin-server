package com.gameplat.admin.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description VIP会员签到汇总出参
 * @date 2021/11/24
 */

@Data
public class MemberVipSignStatisVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("会员账号")
    private String userName;

    @ApiModelProperty("签到次数")
    private Integer signCount;

    @ApiModelProperty(value = "最后签到时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createTime;


}
