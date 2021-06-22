package com.gameplat.admin.enums;

/**
 * 是否
 */
public enum TrueFalseEnum {

	NO(0, "否"), YES(1, "是");
	private Integer value;
	private String desc;
	
	private TrueFalseEnum(Integer value, String desc){
		this.value = value;
		this.desc = desc;
	}

	public Integer getValue() {
		return value;
	}

	public String getDesc() {
		return desc;
	}
	
	
}
