package com.gameplat.admin.model.vo.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
*
* @author 沙漠
* @date 2020年7月17日 下午2:36:56
*/
@Data
@ApiModel(value="SysUserAuthVO对象", description="用户认证")
public class SysUserAuthVO implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "身份认证中心url")
	    private String idcardAuthUrl;

	    @ApiModelProperty(value = "身份认证中心key")
	    private String idcardAuthKey;

	    @ApiModelProperty(value = "银行认证中心url")
	    private String bankAuthUrl;

	    @ApiModelProperty(value = "银行认证中心key")
	    private String bankAuthKey;

	    @ApiModelProperty(value = "创建人")
	    private String createBy;

	    @ApiModelProperty(value = "创建时间")
	    private Date createTime;

	    @ApiModelProperty(value = "更新人")
	    private String updateBy;

	    @ApiModelProperty(value = "更新时间")
	    private Date updateTime;

}
