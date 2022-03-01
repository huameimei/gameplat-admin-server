package com.gameplat.admin.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gameplat.admin.util.Date2LongSerializerUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description 金币
 * @date 2022/3/1
 */
@Data
public class MemberGoldCoinRecordVO implements Serializable {

    private Integer id;

    @ApiModelProperty(value = "来源类型（待定）1 获取成长值、2 爆料扣款")
    private Integer sourceType;

    @ApiModelProperty(value = "账号")
    private String account;

    @ApiModelProperty(value = "变动前金币数")
    private Integer beforeBalance;

    @ApiModelProperty(value = "变动金币数")
    private Integer amount;

    @ApiModelProperty(value = "变动后金币数")
    private Integer afterBalance;

    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = Date2LongSerializerUtils.class)
    private Date createTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
