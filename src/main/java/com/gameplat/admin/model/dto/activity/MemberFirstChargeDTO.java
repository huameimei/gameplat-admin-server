package com.gameplat.admin.model.dto.activity;

import com.live.cloud.backend.model.activity.vo.MemberChargeDiscountVO;
import com.live.cloud.common.util.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dozer.Mapping;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class MemberFirstChargeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "页面大小")
    private Integer pageSize;

    @ApiModelProperty(value = "第几页")
    private Integer pageNum;

    @ApiModelProperty(value = "主键")
    private Long[] ids;

    @ApiModelProperty(value = "主键")
    @Mapping(value = "chargeId")
    private Long id;

    @ApiModelProperty(value = "开始时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "首充类型")
    private Integer chargeType;

    @ApiModelProperty(value = "每日首充条件")
    private String chargeTerm;

    @ApiModelProperty(value = "首充标题")
    private String chargeTitle;

    @ApiModelProperty(value = "首充展示位置")
    private String chargeDisplay;

    @ApiModelProperty(value = "状态 0下线 1上线")
    private Integer status;

    @ApiModelProperty(value = "被删除的优惠列表")
    private Long[] delDiscountIdList;

    @ApiModelProperty(value = "优惠列表")
    private List<MemberChargeDiscountVO> discountlist;

    public void setBeginTime(String beginTime) {
        this.beginTime = DateUtil.StringToDate(beginTime, DateUtil.SIMPLE);
    }

    public void setEndTime(String endTime) {
        this.endTime = DateUtil.StringToDate(endTime, DateUtil.SIMPLE);
    }

}


