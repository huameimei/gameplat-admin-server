package com.gameplat.admin.model.dto;

import java.io.Serializable;
import java.util.Map;
import lombok.Data;

@Data
public class LimitInfoDTO implements Serializable {

	private String name;

	private Map<String, Object> params;
}
