package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @Description : VIP会员签到历史记录
 * @Author : lily
 * @Date : 2021/12/07
 */

@Data
@TableName("member_vip_sign_history")
public class MemberVipSignHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("会员ID")
    private Long userId;

    @ApiModelProperty("会员账号")
    private String userName;

    @ApiModelProperty(value = "签到时间")
    private Date signTime;

    @ApiModelProperty(value = "签到IP")
    private String signIp;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
