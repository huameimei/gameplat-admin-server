package com.gameplat.admin.model.dto;

import com.gameplat.base.common.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

/**
 * @author lyq
 * @Description 转盘DTO
 * @date 2020年5月28日 上午11:37:09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActivityTurntableDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页面大小")
    private Integer pageSize;

    @ApiModelProperty(value = "第几页")
    private Integer pageNum;

    @ApiModelProperty(value = "主键")
    private Long[] ids;

    @ApiModelProperty(value = "主键")
    @Mapping(value = "turntableId")
    private Long turntableId;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "类型（数据字典：game游戏，live：直播）")
    private String type;

    @ApiModelProperty(value = "展示位置")
    private String display;

    @ApiModelProperty(value = "转1次消耗")
    private Integer turnOne;

    @ApiModelProperty(value = "转10此消耗")
    private Integer turnTen;

    @ApiModelProperty(value = "转1次幸运值")
    private Integer turnOneLucky;

    @ApiModelProperty(value = "转10次幸运值")
    private Integer turnTenLucky;

    @ApiModelProperty(value = "总幸运值")
    private Integer totalLucky;

    @ApiModelProperty(value = "幸运值满赠送")
    private Long luckyGive;

    @ApiModelProperty(value = "状态 0下线 1上线")
    private Integer status;

    @ApiModelProperty(value = "转盘标题")
    private String turnTitle;

    @ApiModelProperty(value = "红包时间(周一到周日)")
    private String weekTime;

    @ApiModelProperty(value = "被删除的奖品列表")
    private Long[] deleteIdList;

    @ApiModelProperty(value = "奖品列表")
    private List<ActivityPrizeDTO> activityPrize;

    public void setBeginTime(String beginTime) {
        this.beginTime = DateUtil.strToDate(beginTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
    }

    public void setEndTime(String endTime) {
        this.endTime = DateUtil.strToDate(endTime, DateUtil.YYYY_MM_DD_HH_MM_SS);
    }


}
