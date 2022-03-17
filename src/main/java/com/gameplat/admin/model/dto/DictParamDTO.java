package com.gameplat.admin.model.dto;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="SysDictData对象", description="字典数据表")
public class DictParamDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private JSONObject jsonData;

	private String dictType;

	private Integer gameBlacklistId;

	private String gameBlacklistHint;

}
