package com.gameplat.admin.enums.limit;

/**
 * 默认enum
 */
public enum VisibleTypeEnum {

	SHOW(0, "可见"),
	REQUIRED(1, "必填"),
	HIDE(2, "隐藏");
	private Integer value;
	private String name;
	
	private VisibleTypeEnum(Integer value, String name){
		this.value = value;
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
	
}
