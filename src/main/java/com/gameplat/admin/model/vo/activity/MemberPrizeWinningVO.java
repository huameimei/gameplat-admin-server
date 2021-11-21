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
 * 转盘中奖记录
 * </p>
 *
 * @author 沙漠
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MemberPrizeWinningVO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "主键")
    @Mapping(value = "winningId")
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "奖品类型")
    private String prizeType;

    @ApiModelProperty(value = "奖品名称")
    private String prizeName;

    @ApiModelProperty(value = "奖品等级")
    private String prizeLevel;

    @ApiModelProperty(value = "中获数量")
    private Integer num;

    @ApiModelProperty(value = "是否被领取 0否 1是")
    private Integer isGet;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "用户头像")
    private String headImgUrl;

    @ApiModelProperty(value = "创建时间戳")
    private Long longCreateTime;

}

