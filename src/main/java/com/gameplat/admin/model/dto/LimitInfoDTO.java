package com.gameplat.admin.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class LimitInfoDTO implements Serializable {

	private String name;

	private Map<String, Object> params;
}
