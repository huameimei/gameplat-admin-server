package com.gameplat.admin.model.domain.activity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author bhf
 * @Description 用户幸运值实体层
 * @date 2020-06-12 19:15:07
 */
@Data
public class MemberLuckValue implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "用户id")
	private Long userId;

	@ApiModelProperty(value = "转盘id")
	private Long turntableId;

	@ApiModelProperty(value = "幸运值")
	private Integer luckyValue;

	@ApiModelProperty(value = "创建人")
	private String createBy;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "更新人")
	private String updateBy;

	@ApiModelProperty(value = "更新时间")
	private Date updateTime;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "主键集合")
	private List<Long> ids;
}
