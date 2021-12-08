package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * @author lily
 * @description 会员俸禄派发详情出参
 * @date 2021/11/27
 */
@Data
public class MemberWealDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "福利表主键")
    private Long wealId;

    @ApiModelProperty(value = "会员id")
    private Long userId;

    @ApiModelProperty(value = "会员账号")
    private String userName;

    @ApiModelProperty(value = "会员层级")
    private Integer level;

    @ApiModelProperty(value = "派发金额")
    private BigDecimal rewordAmount;

    @ApiModelProperty(value = "1：未派发   2：已派发  3:已回收")
    private Integer status;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")

    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
