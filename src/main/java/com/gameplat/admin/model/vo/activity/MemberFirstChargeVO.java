package com.gameplat.admin.model.vo.activity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 每日首充表
 * </p>
 *
 * @author 沙漠
 * @since 2020-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberFirstChargeVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @Mapping(value = "chargeId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "开始时间")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "首充类型")
    private Integer chargeType;

    @ApiModelProperty(value = "每日首充条件")
    private String chargeTerm;

    @ApiModelProperty(value = "首充展示位置")
    private String chargeDisplay;

    @ApiModelProperty(value = "首充标题")
    private String chargeTitle;

    @ApiModelProperty(value = "状态 0下线 1上线")
    private Integer status;

    /*@ApiModelProperty(value = "优惠列表")
    private List<MemberChargeDiscountVO> discountlist;*/


}
