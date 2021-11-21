package com.gameplat.admin.model.dto.activity;

import lombok.Data;

@Data
public class WeightCategoryDTO {
	private Long num; // 奖品编号
	private String category; // 奖项名称
	private Integer weight; // 权重（抽中概率）

	public WeightCategoryDTO() {
		super();
	}

	public WeightCategoryDTO(Long num, String category, Integer weight) {
		super();
		this.setNum(num);
		this.setCategory(category);
		this.setWeight(weight);
	}

}
