package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lily
 * @description VIPBanner图
 * @date 2022/1/15
 */

@Data
@TableName("member_growth_banner")
public class MemberGrowthBanner implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "终端: 0 WEB  1 H5  2 ANDRIOD  3 IOS")
    private Integer cilentType;

    @ApiModelProperty(value = "页面使用位置: 0 H5 ANDRIOD IOS 对应福利中心轮播   2 对应VIP详情轮播    PC 1对应轮播")
    private Integer areaType;

    @ApiModelProperty(value = "路径")
    private String src;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;
}
