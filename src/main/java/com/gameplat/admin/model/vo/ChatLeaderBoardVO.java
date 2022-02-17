package com.gameplat.admin.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class ChatLeaderBoardVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;

	private String account;

	private String nickName;

	private Double winMoney;

	private String avatar;

	//数据类型:1-后台添加，0-统计添加
	private Integer type;

	private Date createTime;
}
