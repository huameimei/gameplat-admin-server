package com.gameplat.admin.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gameplat.base.common.json.JsonUtils;
import java.util.Date;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

/**
 * 全局限制数据配置
 *
 */
@Data
@TableName("limit_info")
public class LimitInfo<T extends Object> implements java.io.Serializable {


	/** 配置名称(如rechLimit充值限制,提现限制) */
	private String name;
	/** 配置值,对象为json格式*/
	private String value;
	/** 值对应的class如(java.lang.Integer)*/
	private String valueClass;
	/** 备注*/
	private String remarks;
	/** 操作人员*/
	private String operator;
	/** 添加时间*/
	private Date addTime;
	/** 修改时间*/
	private Date updateTime;

	@TableField(exist = false)
	private T limit;

	public String getValue() {
		if(StringUtils.isEmpty(this.value)){
				value = JsonUtils.toJson(limit);
		}
		return value;
	}

	@JsonIgnore
	@SuppressWarnings("unchecked")
	public T getLimit() {
		try {
			 Class<?> forName = Class.forName(this.valueClass);
			return (T)JsonUtils.parse(this.getValue(), forName);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
		}
		return limit;
	}
}
