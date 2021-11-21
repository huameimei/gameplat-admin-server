package com.gameplat.admin.model.dto.activity;

import com.gameplat.common.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dozer.Mapping;

import java.io.Serializable;
import java.util.Date;

/**
 * 添加活动DTO
 * @author 沙漠
 * @date 2020年5月28日
 */
@Data
public class MemberActivityDTO implements Serializable {

	private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ids")
    private Long[] ids;

    @ApiModelProperty(value = "主键")
    @Mapping(value = "activityId")
    private Long id;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "是否参与红包雨 0否 1是")
    private Integer isPackage;

    @ApiModelProperty(value = "是否参与每日首充 0否 1是")
    private Integer isCharge;

    @ApiModelProperty(value = "是否参与转盘 0否 1是")
    private Integer isTurntable;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "活动类型")
    private Integer activityType;

    @ApiModelProperty(value = "红包雨")
    private MemberRedPacketDTO memberRedPacketDTO;

    @ApiModelProperty(value = "每日首充")
    private MemberFirstChargeDTO memberFirstChargeDTO;

    @ApiModelProperty(value = "转盘")
    private MemberTurntableDTO memberTurntableDTO;

    public void setBeginTime(String beginTime) {
    	this.beginTime= DateUtil.strToDate(beginTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
    }

    public void setEndTime(String endTime) {
    	this.endTime=DateUtil.strToDate(endTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
    }



}
