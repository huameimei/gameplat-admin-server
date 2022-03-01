package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 金币
 * @date 2022/3/1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("member_gold_coin_record")
public class MemberGoldCoinRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "订单号")
    private Long memberId;

    @ApiModelProperty(value = "玩家名字")
    private String account;

    @ApiModelProperty(value = "来源类型（待定）1 获取成长值、2 爆料扣款")
    private Integer sourceType;

    @ApiModelProperty(value = "变动前金币数")
    private Integer beforeBalance;

    @ApiModelProperty(value = "变动金币数")
    private Integer amount;

    @ApiModelProperty(value = "变动后金币数")
    private Integer afterBalance;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
