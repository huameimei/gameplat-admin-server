package com.gameplat.admin.model.dto.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 公共查询DTO
 * </p>
 *
 * @author 沙漠
 * @since 2020-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="公共查询DTO", description="公共查询DTO")
public class QueryCommomDTO implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "页面大小")
    private Integer pageSize;

    @ApiModelProperty(value = "第几页")
    private Integer pageNum;

    @ApiModelProperty(value = "根据哪个字段排序")
    private String orderByColumn;

    @ApiModelProperty(value = "排序方式（asc 或者 desc）")
    private String isAsc;

}

