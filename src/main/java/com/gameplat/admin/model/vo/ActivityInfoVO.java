package com.gameplat.admin.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 活动VO
 *
 * @author kenvin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ActivityInfoVO", description = "活动VO")
public class ActivityInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Mapping(value = "id")
    @ApiModelProperty(value = "编号")
    private String id;

    @ApiModelProperty(value = "活动标题")
    private String title;

    @ApiModelProperty(value = "活动类型")
    private Long type;

    @ApiModelProperty(value = "活动类型名称")
    private String typeName;

    @ApiModelProperty(value = "活动类型编码")
    private String typeCode;

    @ApiModelProperty(value = "APP副图片路径")
    private String viceAppPic;

    @ApiModelProperty(value = "PC副图片路径")
    private String vicePcPic;

    @ApiModelProperty(value = "活动对象")
    private String activeObject;

    @ApiModelProperty(value = "活动内容")
    private String activeContent;

    @ApiModelProperty(value = "活动规则")
    private String activeRule;

    @ApiModelProperty(value = "申请方式")
    private Integer applyType;

    @ApiModelProperty(value = "申请流程")
    private String applyProcess;

    @ApiModelProperty(value = "活动有效期")
    private String validPeriod;

    @ApiModelProperty(value = "活动状态")
    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "关联活动大厅ID")
    private Long activityLobbyId;

    @ApiModelProperty(value = "活动有效状态")
    private Integer validStatus;

    @ApiModelProperty(value = "活动开始时间")
    private String beginTime;

    @ApiModelProperty(value = "活动结束时间")
    private String endTime;

}
