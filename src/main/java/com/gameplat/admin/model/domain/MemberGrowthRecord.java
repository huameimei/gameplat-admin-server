package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @author lily
 * @description 成长值记录实体类
 * @date 2021/11/23
 */

@Data
@TableName("member_growth_record")
public class MemberGrowthRecord implements Serializable {

    @ApiModelProperty(value = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会员id")
    private Long userId;

    @ApiModelProperty(value = "会员账号")
    private String userName;

    @ApiModelProperty(value = "变动游戏分类编码")
    private String kindCode;

    @ApiModelProperty(value = "变动游戏分类名称")
    private String kindName;

    @ApiModelProperty(value = "类型：0:充值  1:签到 2:投注打码量 3:后台修改 4:完善资料 5：绑定银行卡")
    private Integer type;

    @ApiModelProperty(value = "变动前的等级")
    private Integer oldLevel;

    @ApiModelProperty(value = "变动后的等级")
    private Integer currentLevel;

    @ApiModelProperty(value = "变动倍数")
    private Double changeMult;

    @ApiModelProperty(value = "变动的成长值")
    private Integer changeGrowth;

    @ApiModelProperty(value = "变动前的成长值")
    private Integer oldGrowth;

    @ApiModelProperty(value = "变动后的成长值")
    private Integer currentGrowth;

    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
